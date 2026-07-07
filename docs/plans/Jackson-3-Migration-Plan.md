# Jackson 3 Migration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Migrate JSONata4Java from Jackson 2.x (`com.fasterxml.jackson.*`) to Jackson 3.x (`tools.jackson.*`) and release as version 3.0.0.

**Architecture:** Hard cutover. Both Jackson 2.x and 3.x deps are currently present in `pom.xml`, so we exploit that overlap: add the Jackson 3 XML dataformat, migrate all source/test code to `tools.jackson`, get it compiling and green against Jackson 3, then remove the 2.x deps and bump the version. This keeps every task boundary in a compiling, tested state.

**Tech Stack:** Java 17, Maven, Jackson 3.2.0 (`tools.jackson.core:jackson-databind`, `tools.jackson.dataformat:jackson-dataformat-xml`), ANTLR4, JUnit 4, woodstox-core (Stax backend for XML).

## Global Constraints

- Target Jackson version: **3.2.0** for both `jackson-databind` and `jackson-dataformat-xml`.
- Project version becomes **3.0.0** (from `2.6.4`).
- `jackson-annotations` is **not** renamed — never rewrite `com.fasterxml.jackson.annotation` (none present today).
- No behavioral changes beyond what the migration requires; if a Jackson 3 default flip changes test output, restore prior behavior via builder config rather than accept the new default.
- No `module-info.java` exists; do not add one.
- Reference spec: `docs/specs/Jackson-3-Migration-Design.md`.
- Commit messages end with the `Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>` trailer.

---

### Task 1: Add Jackson 3 XML dataformat dependency

Adds the Jackson 3 XML library alongside the existing deps so that when `TesterUI` is renamed to `tools.jackson.dataformat.xml` in Task 2 it has something to compile against. The 2.x deps stay for now; the code still compiles because it still imports `com.fasterxml.jackson.*`.

**Files:**
- Modify: `pom.xml` (dependencies section, near the existing `tools.jackson.core:jackson-databind` entry around line 105)

**Interfaces:**
- Consumes: nothing.
- Produces: the Maven coordinate `tools.jackson.dataformat:jackson-dataformat-xml:3.2.0` available on the compile classpath for later tasks.

- [ ] **Step 1: Add the dependency**

In `pom.xml`, immediately after the existing `tools.jackson.core:jackson-databind` dependency block, add:

```xml
    <!-- Source: https://mvnrepository.com/artifact/tools.jackson.dataformat/jackson-dataformat-xml -->
    <dependency>
        <groupId>tools.jackson.dataformat</groupId>
        <artifactId>jackson-dataformat-xml</artifactId>
        <version>3.2.0</version>
        <scope>compile</scope>
    </dependency>
```

- [ ] **Step 2: Verify it resolves and the project still compiles**

Run: `mvn -q -DskipTests clean compile`
Expected: BUILD SUCCESS (code is still on `com.fasterxml.jackson.*`, both stacks present).

- [ ] **Step 3: Confirm the coordinate is on the tree**

Run: `mvn -q dependency:tree | grep jackson-dataformat-xml`
Expected: shows both `com.fasterxml.jackson.dataformat:jackson-dataformat-xml:jar:2.22.0` and `tools.jackson.dataformat:jackson-dataformat-xml:jar:3.2.0`.

- [ ] **Step 4: Commit**

```bash
git add pom.xml
git commit -m "Add Jackson 3 XML dataformat dependency (#430)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 2: Migrate all source and test code to `tools.jackson`

The core of the migration. Run the scripted package rename across all Java sources, then hand-fix the API-change hotspots until the whole project compiles and the full test suite is green against Jackson 3. The task deliverable is a green `mvn test` with all code on `tools.jackson.*`.

**Files:**
- Modify: every `.java` under `src/main/java` and `src/test/java` that imports Jackson (~103 files) — bulk rename.
- Modify (hand): `src/main/java/com/api/jsonata4java/Tester.java`
- Modify (hand): `src/main/java/com/api/jsonata4java/testerui/TesterUI.java`
- Modify (hand, as compiler dictates): `TesterTimeBox.java`, `Test.java`, `ExpressionsVisitor.java`, `issues/Issue180.java`

**Interfaces:**
- Consumes: `tools.jackson.dataformat:jackson-dataformat-xml:3.2.0` (Task 1) and `tools.jackson.core:jackson-databind:3.2.0` (already in pom).
- Produces: entire codebase importing `tools.jackson.*`; public API now exposes `tools.jackson.databind.JsonNode`. No new public method names introduced.

- [ ] **Step 1: Run the scripted package rename**

Longest-prefix-first so `dataformat.xml` and `databind`/`core` don't double-rewrite. Run from the repo root:

```bash
find src/main/java src/test/java -name '*.java' -print0 | xargs -0 perl -pi -e '
  s/\Qcom.fasterxml.jackson.dataformat.xml\E/tools.jackson.dataformat.xml/g;
  s/\Qcom.fasterxml.jackson.databind\E/tools.jackson.databind/g;
  s/\Qcom.fasterxml.jackson.core\E/tools.jackson.core/g;
'
```

- [ ] **Step 1b: Apply Jackson 3 API class/method renames (verified against the 3.2.0 jar)**

Jackson 3 renamed several `JsonNode` types and methods, not just packages. These were confirmed by inspecting `jackson-databind-3.2.0.jar` / `jackson-core-3.2.0.jar`:

- `TextNode` → `StringNode` (the class `tools.jackson.databind.node.TextNode` does not exist; `StringNode` replaces it). Affects imports, `new TextNode(...)`, `TextNode.valueOf(...)`, and `(TextNode)` casts. ~106 refs.
- `tools.jackson.core.JsonProcessingException` → `tools.jackson.core.JacksonException` (JsonProcessingException is gone from core; `JacksonException` is the unchecked base). Affects imports and `catch` clauses. ~28 refs.
- `.fieldNames()` (returned `Iterator<String>` in 2.x) → `.propertyNames()` (returns `Collection<String>` in 3.x). To preserve `Iterator`-based loops, replace `X.fieldNames()` with `X.propertyNames().iterator()`. ~21 calls.
- `.elements()` (returned `Iterator<JsonNode>` in 2.x) → `.values()` (returns `Collection<JsonNode>` in 3.x). Replace `X.elements()` with `X.values().iterator()`. ~5 calls.
- `.fields()` → `.properties()` if present. (This codebase has **0** `.fields()` calls — nothing to do, but do not introduce any.)
- NOTE: `textValue()` and `asText()` **still exist** on `JsonNode` in 3.x — do NOT rewrite those.

Apply the safe global renames with word-boundary care, then let the compiler catch the rest:

```bash
find src/main/java src/test/java -name '*.java' -print0 | xargs -0 perl -pi -e '
  s/\bTextNode\b/StringNode/g;
  s/\bJsonProcessingException\b/JacksonException/g;
  s/\.fieldNames\(\)/.propertyNames().iterator()/g;
  s/\.elements\(\)/.values().iterator()/g;
'
```

Note: `TextNode` → `StringNode` also renames the import line `import tools.jackson.databind.node.TextNode;` to `import tools.jackson.databind.node.StringNode;`, which is correct. Watch for identifiers that merely contain "TextNode" as a substring — the `\b` boundaries prevent partial-word hits, but verify at compile time.

- [ ] **Step 2: Confirm no `com.fasterxml.jackson` core/databind/xml references remain**

Run: `grep -rn "com.fasterxml.jackson.core\|com.fasterxml.jackson.databind\|com.fasterxml.jackson.dataformat" src/ || echo CLEAN`
Expected: `CLEAN` (annotations, if any ever appear, are intentionally left; none exist today).

- [ ] **Step 3: Compile to surface the remaining hotspots**

Run: `mvn -q -DskipTests clean compile 2>&1 | tee /tmp/j3-compile.log | grep -E "ERROR|\.java:" | head -50`
Expected: FAILS — after Step 1b the remaining errors are concentrated in `Tester.java` (`getFactory().configure`, `JsonWriteFeature.mappedFeature()`) and `TesterUI.java` (`xmlMapper.enable`/`.configure`, `ToXmlGenerator.Feature`), plus any stragglers from the method renames. Note each reported file:line and fix iteratively.

- [ ] **Step 4: Fix `Tester.java` mapper construction**

Jackson 3 mappers are immutable — `getFactory().configure(...)` and the 2.x `JsonWriteFeature.mappedFeature()` bridge are gone. Replace the two lines at the top of `main(...)`:

Old:
```java
        ObjectMapper mapper = new ObjectMapper();
        mapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
```
New:
```java
        JsonMapper mapper = JsonMapper.builder()
            .enable(JsonWriteFeature.ESCAPE_NON_ASCII)
            .build();
```

Update imports in `Tester.java`: remove `import tools.jackson.databind.ObjectMapper;`, add `import tools.jackson.databind.json.JsonMapper;`. Keep `import tools.jackson.core.json.JsonWriteFeature;` (the rename already moved it out of `com.fasterxml`). If the compiler reports `JsonWriteFeature` is not in `tools.jackson.core.json`, follow the compiler's suggested package (Jackson 3 relocated some feature enums) — it is a streaming write feature under `tools.jackson.core`.

- [ ] **Step 5: Fix `TesterUI.java` mapper fields and XML config**

The `xmlMapper` field needs all its features set at build time (immutable mapper). Find the field declarations (around lines 75-76):

Old:
```java
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();
```
New:
```java
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = XmlMapper.builder()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
        .enable(ToXmlGenerator.Feature.WRITE_XML_1_1)
        .build();
```

Then delete the now-redundant mutating calls:
- In the constructor (around line 108): delete the line `xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);`
- In `jsonToXml(...)` (around lines 488-490): delete the three lines
  ```java
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true);
  ```
  Leave the rest of `jsonToXml` (`ObjectWriter ow = xmlMapper.writer().withRootName("root");` etc.) intact.

If `new ObjectMapper()` is rejected by the compiler, replace it with `new JsonMapper()` (import `tools.jackson.databind.json.JsonMapper`) — a `JsonMapper` is-a `ObjectMapper` in Jackson 3. If `XmlMapper.builder().enable(...)` does not accept a given feature enum, consult the compiler message for the correct builder method; the three features are: pretty-print (`SerializationFeature.INDENT_OUTPUT`), XML declaration, and XML 1.1.

- [ ] **Step 6: Recompile and fix any remaining API breaks**

Run: `mvn -q -DskipTests clean compile 2>&1 | tee /tmp/j3-compile.log | tail -40`
Expected: iterate until BUILD SUCCESS. Likely remaining items: unreachable `catch (JsonProcessingException e)` clauses — Jackson 3 made it unchecked, so a lone catch of it is still legal, but if the compiler flags any catch as unreachable, change that type to `tools.jackson.core.JacksonException`. Do not touch `IOException` catches. Files that may need this: `Tester.java`, `TesterTimeBox.java`, `Test.java`, `TesterUI.java`, `ExpressionsVisitor.java`, `issues/Issue180.java`.

- [ ] **Step 7: Run the full test suite**

Run: `mvn -q test 2>&1 | tail -30`
Expected: BUILD SUCCESS. This runs the unit tests and `AgnosticTestSuite` (the regression safety net), including the 16 migrated test files.

- [ ] **Step 8: If any test regressed, restore prior behavior**

If a test fails due to a Jackson 3 default change (e.g. `WRITE_DATES_AS_TIMESTAMPS`, `FAIL_ON_UNKNOWN_PROPERTIES`), fix by configuring the relevant mapper's builder to the 2.x behavior at its construction site — not by editing the test's expectation. Re-run `mvn -q test` until green.

- [ ] **Step 9: Commit**

```bash
git add -A
git commit -m "Migrate source and tests to Jackson 3 (tools.jackson) (#430)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 3: Remove Jackson 2.x dependencies and bump version to 3.0.0

With all code on Jackson 3 and tests green, drop the now-unused 2.x deps and cut the major version. Per `ReleaseProcedure.txt`, the version string lives in six places across five files — all must move from `2.6.4` to `3.0.0`.

**Files:**
- Modify: `pom.xml` — remove two dependency blocks, change `<version>` (1 place, line ~29).
- Modify: `README.md` — version string in 3 places (lines 17, 54, 55).
- Modify: `tester.sh` — jar name (line 2).
- Modify: `testerui.sh` — jar name (line 2).
- Modify: `testerui.cmd` — jar name (line 1).

**Interfaces:**
- Consumes: green build from Task 2.
- Produces: a classpath with no `com.fasterxml.jackson.core`/`databind`/`dataformat.xml` 2.x jars; project version `3.0.0` consistent across pom, README, and launch scripts.

- [ ] **Step 1: Remove the Jackson 2.x dependency blocks**

In `pom.xml`, delete these two blocks:
```xml
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.22.0</version>
    </dependency>
```
```xml
    <dependency>
      <groupId>com.fasterxml.jackson.dataformat</groupId>
      <artifactId>jackson-dataformat-xml</artifactId>
      <version>2.22.0</version>
    </dependency>
```
Leave `com.fasterxml.woodstox:woodstox-core` in place — Jackson 3 XML still uses it.

- [ ] **Step 2: Bump the project version in `pom.xml`**

Change the top-level project version (the `<version>` directly under `<artifactId>JSONata4Java</artifactId>`, around line 29 — not a dependency or plugin version):
Old: `  <version>2.6.4</version>`
New: `  <version>3.0.0</version>`

- [ ] **Step 3: Bump the version string in `README.md`, `tester.sh`, `testerui.sh`, `testerui.cmd`**

Per `ReleaseProcedure.txt`, update the version in the README (3 places) and the three launch scripts (1 each). Run from the repo root:

```bash
perl -pi -e 's/\Q2.6.4\E/3.0.0/g' README.md tester.sh testerui.sh testerui.cmd
```

Verify:
```bash
grep -rn "2\.6\.4" README.md tester.sh testerui.sh testerui.cmd || echo CLEAN
```
Expected: `CLEAN`. And confirm the new strings landed:
```bash
grep -n "3\.0\.0" README.md tester.sh testerui.sh testerui.cmd
```
Expected: `README.md` lines 17/54/55, `tester.sh` line 2, `testerui.sh` line 2, `testerui.cmd` line 1.

- [ ] **Step 4: Verify no Jackson 2.x remains on the dependency tree**

Run: `mvn -q dependency:tree | grep "com.fasterxml.jackson" || echo NONE`
Expected: only `com.fasterxml.woodstox:woodstox-core` may appear; no `com.fasterxml.jackson.core`, `...databind`, or `...dataformat.jackson-*` 2.x entries. (Note: `jackson-annotations` under `com.fasterxml.jackson` may appear transitively via Jackson 3 — that is expected and correct.)

- [ ] **Step 5: Full clean build + tests**

Run: `mvn -q clean test 2>&1 | tail -30`
Expected: BUILD SUCCESS with only Jackson 3 (plus jackson-annotations 2.x, woodstox) on the classpath.

- [ ] **Step 6: Commit**

```bash
git add pom.xml README.md tester.sh testerui.sh testerui.cmd
git commit -m "Drop Jackson 2.x deps, bump to 3.0.0 (#430)

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>"
```

---

### Task 4: Verify OSGi manifest and exercise CLI/XML paths

Confirm the packaged artifact's OSGi metadata now imports `tools.jackson.*`, and smoke-test the two entry points that carry the most API churn.

**Files:**
- No source changes expected. If verification fails, fixes route back to Task 2/3.

**Interfaces:**
- Consumes: green build from Task 3.
- Produces: verified runnable artifact; confirmation that no `com.fasterxml.jackson` import remains in the bundle manifest.

- [ ] **Step 1: Package the jar (runs the bnd OSGi plugin)**

Run: `mvn -q -DskipTests package 2>&1 | tail -15`
Expected: BUILD SUCCESS; produces `target/JSONata4Java-3.0.0.jar`.

- [ ] **Step 2: Inspect the generated OSGi Import-Package header**

Run: `unzip -p target/JSONata4Java-3.0.0.jar META-INF/MANIFEST.MF | tr -d '\r' | grep -A40 "Import-Package"`
Expected: imports reference `tools.jackson.*`; no `com.fasterxml.jackson.core`/`databind`/`dataformat.xml` imports (a `com.fasterxml.jackson.annotation` import, if present, is acceptable).

- [ ] **Step 3: Smoke-test the Tester CLI**

Run:
```bash
echo "q" | java -cp target/JSONata4Java-3.0.0-jar-with-dependencies.jar com.api.jsonata4java.Tester null
```
Expected: prints the pretty-printed default JSON (`Using json:` block) then `Goodbye`, with no exception/stack trace.

- [ ] **Step 4: Verify the XML round-trip logic (TesterUI)**

`TesterUI` is a Swing app; verify its `jsonToXml`/`xmlToJson` logic compiles and runs headlessly by confirming the package build already exercised `XmlMapper.builder()`. If a manual check is desired, run the UI on a machine with a display:
```bash
java -cp target/JSONata4Java-3.0.0-jar-with-dependencies.jar com.api.jsonata4java.testerui.TesterUI
```
Expected: window opens; using the JSON→XML and XML→JSON conversion produces indented XML with an XML declaration and valid JSON, respectively. (Skip if no display is available; the compile in Task 2 already validated the API usage.)

- [ ] **Step 5: Final confirmation (no commit unless fixes were made)**

Run: `git status`
Expected: clean tree (Tasks 1-3 already committed). If Step 2/3 forced a code fix, commit it with a message referencing `#430`.

---

## Notes for the implementer

- The rename in Task 2 Step 1 is the bulk of the change but is purely mechanical; the real work is Steps 4-8. Do not skip the compile-and-iterate loop.
- Jackson 3's exact builder method names for XML features (`ToXmlGenerator.Feature`) and the home of `JsonWriteFeature` should be confirmed against compiler output rather than assumed — the plan gives the expected shape, the compiler is the authority.
- Keep `woodstox-core`: removing it will break XML parsing at runtime even though compilation succeeds.
