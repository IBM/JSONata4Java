# Migrate to Jackson 3 (`tools.jackson`) — JSONata4Java 3.0.0

**Issue:** [#430](https://github.com/IBM/JSONata4Java/issues/430) — Migrate code from
`com.fasterxml.jackson.core` to `tools.jackson.core`.

## Summary

Jackson 3.0 renamed both its Maven group-ids and Java packages from `com.fasterxml.jackson`
to `tools.jackson`, made the core exception hierarchy unchecked, and moved mapper
configuration to an immutable builder pattern. JSONata4Java exposes
`com.fasterxml.jackson.databind.JsonNode` throughout its public API, so migrating to Jackson 3
is a breaking change and warrants a new major version.

The project's `pom.xml` already stages the transition by listing both the Jackson 2.x and
Jackson 3.x dependencies. This work completes the cutover: remove the 2.x stack, migrate all
source and test code to `tools.jackson`, and release as **3.0.0**.

## Decisions

- **Strategy: hard cutover.** Replace all `com.fasterxml.jackson.*` usage with `tools.jackson.*`
  and drop the 2.x dependencies entirely. No dual-support/abstraction layer (JsonNode is woven
  too deeply into the public API to make that worthwhile — YAGNI).
- **Version: 3.0.0.** Bump from `2.6.4`. The public-API break (JsonNode package change) requires
  a major version.
- **XML in scope.** Port the `XmlMapper`/`ToXmlGenerator` usage to Jackson 3's
  `tools.jackson.dataformat.xml`.
- **CLI/UI helpers in scope.** `Tester`, `TesterTimeBox`, `TesterUI`, and `Test` are migrated
  along with the core library, even though they carry most of the API churn.
- **Target dependency version: 3.2.0** (current Jackson 3 line; confirmed published on Maven
  Central for both `jackson-databind` and `jackson-dataformat-xml`).

## Scope

103 Java files reference Jackson today: 87 under `src/main/java`, 16 under `src/test/java`.
The overwhelming majority only use JSON node types whose class names are unchanged in Jackson 3
— only the package prefix moves.

## Dependency changes (`pom.xml`)

| Action | Coordinates |
| --- | --- |
| Remove | `com.fasterxml.jackson.core:jackson-databind:2.22.0` |
| Remove | `com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.22.0` |
| Keep   | `tools.jackson.core:jackson-databind:3.2.0` (already present) |
| Add    | `tools.jackson.dataformat:jackson-dataformat-xml:3.2.0` |
| Keep   | `com.fasterxml.woodstox:woodstox-core:7.2.1` (Jackson 3 XML still uses Stax/woodstox) |
| Keep   | `re2j` (test), `gson`, `commons-text`, `antlr4-runtime`, `spring-context` (test), `junit` (test) |
| Change | `<version>` `2.6.4` → `3.0.0` |

Notes:
- **Version string lives in more than `pom.xml`.** Per `ReleaseProcedure.txt`, the release
  version appears in six places across five files, all of which move `2.6.4` → `3.0.0`:
  `pom.xml` (1), `README.md` (3 — lines 17, 54, 55), `tester.sh` (1), `testerui.sh` (1),
  `testerui.cmd` (1). The script/README occurrences are the `JSONata4Java-<version>...jar` names.
- `jackson-core` is pulled in transitively by `jackson-databind`; no direct entry needed.
- **`jackson-annotations` was NOT renamed** — it stays under `com.fasterxml.jackson.annotation`.
  The codebase has no annotation imports, so this is a non-issue here, but do not blindly
  rewrite `com.fasterxml.jackson.annotation` should it appear.

## Migration approach

Execute in three passes so the risky hand-edits stay isolated from the bulk mechanical change.

### Pass 1 — Mechanical package rename (~103 files)

Scripted find-replace across `src/main/java` and `src/test/java`, longest-prefix first to avoid
double rewriting:

1. `com.fasterxml.jackson.dataformat.xml` → `tools.jackson.dataformat.xml`
2. `com.fasterxml.jackson.databind`      → `tools.jackson.databind`
3. `com.fasterxml.jackson.core`          → `tools.jackson.core`

This covers all the node types (`JsonNode`, `JsonNodeFactory`, `ArrayNode`, `ObjectNode`,
`TextNode`, `LongNode`, `IntNode`, `DoubleNode`, `FloatNode`, `BooleanNode`, `NullNode`,
`POJONode`, `ValueNode`, `NumericNode`, `JsonNodeType`) and the streaming/databind entry points,
all of which keep their simple class names in Jackson 3.

### Pass 2 — Hand-fix API-change hotspots (< 10 files)

These sites use APIs that changed shape in Jackson 3 and will not compile after a pure rename.

**Mapper configuration → immutable builder.** In Jackson 3, mappers are immutable; the mutating
`enable`/`configure`/`getFactory().configure(...)` methods are gone. All affected sites are in
CLI/UI helpers, not the core library:

- `Tester.java:124` — `mapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true)`
  → construct via `JsonMapper.builder().enable(JsonWriteFeature.ESCAPE_NON_ASCII).build()`.
  `JsonWriteFeature` moves to `tools.jackson.core`.
- `TesterUI.java:108` — `xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)`
  → set in the `XmlMapper.builder()...build()` chain.
- `TesterUI.java:488–491` — `xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true)`,
  `xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)`,
  `xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true)`
  → fold into the `XmlMapper.builder()` chain (verify the `ToXmlGenerator.Feature` enum location
  under `tools.jackson.dataformat.xml`).

**Plain mapper construction.** Sites that only call `readTree` / `writeValueAsString` on a
`new ObjectMapper()` (in `Binding`, `ExpressionsVisitor`, `ContainsFunction`, `Expression`,
`JSONataUtils`, `Issue180`, `Test`) need only the package rename; keep `new ObjectMapper()` (or
switch to `JsonMapper.shared()` / `new JsonMapper()` if `new ObjectMapper()` proves unavailable).
The exact constructor availability is confirmed at compile time in Pass 3.

**Unchecked exceptions.** `JsonProcessingException`'s parent became `RuntimeException` and core
introduced unchecked `JacksonException`. Catch blocks referencing `JsonProcessingException`
generally still compile because it is now unchecked. Where a `catch (JsonProcessingException e)`
becomes unreachable, or the type is no longer resolvable, replace with
`tools.jackson.core.JacksonException`. Review each multi-catch
(`EvaluateException | JsonProcessingException`) individually. Affected files: `TesterTimeBox`,
`Test`, `Tester`, `TesterUI`, `ExpressionsVisitor`, `Issue180`.

### Pass 3 — Compile, test, verify

- `mvn clean compile` — resolve any remaining API breaks surfaced by the compiler; iterate until
  clean.
- `mvn test` — the existing unit tests plus `AgnosticTestSuite` are the correctness safety net;
  the 16 migrated test files run here too.
- Manually exercise the `Tester` CLI (JSON read/pretty-print path) and, where feasible, the Swing
  `TesterUI` XML↔JSON conversion, since those hold the most API churn.

## Build configuration

- **OSGi (`bnd-maven-plugin`).** The `Export-Package` list references only `com.api.jsonata4java.*`
  and needs no change. Confirm the generated `MANIFEST.MF` `Import-Package` header now imports
  `tools.jackson.*` rather than `com.fasterxml.jackson.*`.
- **JPMS.** No `module-info.java` exists; no module changes required.
- No other plugin configuration references Jackson.

## Out of scope

- Any behavioral changes beyond what the package/API migration requires.
- Adopting new Jackson 3 features (e.g., new default flips such as `FAIL_ON_UNKNOWN_PROPERTIES`)
  beyond preserving existing behavior. If a Jackson 3 default change alters observed test
  behavior, restore the prior behavior via builder configuration rather than accepting the new
  default silently.
- Unrelated refactoring of the touched files.

## Risks / watch-items

- **Default-value flips in Jackson 3** (e.g., `WRITE_DATES_AS_TIMESTAMPS`, `FAIL_ON_UNKNOWN_PROPERTIES`)
  could shift test output. The test suite is the detector; restore prior behavior via builder
  config where a test regresses.
- **`ToXmlGenerator.Feature` and `JsonWriteFeature` relocations** — confirm exact package/enum
  homes at compile time; the builder `enable(...)` overloads may differ from the 2.x
  `configure(feature, boolean)` shape.
- **`new ObjectMapper()` availability** — if the no-arg constructor is unavailable/discouraged in
  3.x, fall back to `JsonMapper.shared()` or `new JsonMapper()`.

## Retained Jackson 2 behaviors (as-built)

Jackson 3 tightened several coercion/parse defaults that would otherwise change observable
behavior. Each site below was fixed to **preserve the Jackson 2 behavior** — no test assertion or
expected value was changed. These are the compatibility decisions a future maintainer should know
about (and can revisit if the project decides to adopt the stricter Jackson 3 semantics):

- **Container coercion — `ArrayUtils.compare` (main).** Jackson 3's no-arg `asText()`/`asString()`
  throws for container nodes (`ObjectNode`/`ArrayNode`); Jackson 2 returned `""`. Fixed by passing
  the default: `asText("")`. `$sort` on non-scalar operands therefore coerces to `""` exactly as
  before instead of throwing.
- **Regex `POJONode` emptiness checks — `MatchFunction`, `ReplaceFunction` (main).** A compiled
  `RegularExpression` is stored in a `POJONode`. Jackson 2's `asText()` returned the POJO's
  `toString()` (non-empty); Jackson 3 throws for a `POJONode`. The empty-pattern checks are now
  guarded by `isTextual()` so the throwing call is never reached for a `POJONode`, while the
  boolean outcome is identical to Jackson 2.
- **Out-of-range `asLong()` — `AgnosticTestSuite` (test harness).** Jackson 3's `DoubleNode.asLong()`
  throws when the value is outside `long` range (e.g. `1.0E46`); Jackson 2 performed a silent
  narrowing cast. Expected-value normalization now uses `(long) d`, replicating Jackson 2 exactly.
- **`FAIL_ON_TRAILING_TOKENS` — `Utils` test mapper (test harness).** Jackson 3 flips this default
  to `true`; Jackson 2 silently ignored content after the first parsed value. The shared test
  mapper disables the feature so existing test inputs parse identically. Note: this tolerates a
  long-standing typo in a test input (`BasicExpressionsTests`, `"[\"h11\", \"h21\"]]"` — a stray
  trailing `]`). The typo was intentionally **not** corrected, to keep the test data byte-for-byte
  unchanged.
- **Unchecked exceptions — several files.** Where Jackson 3 methods no longer throw checked
  `IOException`, `catch (IOException)` blocks over Jackson-only bodies became unreachable and were
  changed to `catch (JacksonException)`.

### Other known items (not changed by this migration)

- **Pre-existing `Tester` REPL NPE.** `Tester.main` (`Tester.java:156`) calls
  `expression.length()` without a null check; `JSONataUtils.prompt()` returns `null` at stdin EOF,
  causing a `NullPointerException` when input ends without a `q` line. This predates the migration
  (authored 2022) and was left as-is.
- **Transitive `jackson-annotations` 2.x.** `tools.jackson.core:jackson-databind:3.2.0` still pulls
  in `com.fasterxml.jackson.core:jackson-annotations` — Jackson 3 continues to publish annotations
  under the `com.fasterxml.jackson.annotation` namespace. This is expected and acceptable; it is not
  a leftover Jackson 2 core/databind/dataformat dependency.
