package com.api.jsonata4java.expressions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.tree.ParseTree;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser;
import com.api.jsonata4java.expressions.generated.MappingExpressionParser.Op_orderbyContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.BooleanNode;

public class OrderByOperator {

    private JsonNodeFactory factory = JsonNodeFactory.instance;

    public ArrayNode evaluate(Op_orderbyContext ctx, ArrayNode in) {
        return orderBy(in, extractOrderByFields(ctx));
    }

    private ArrayNode orderBy(final ArrayNode in, final List<OrderByField> sortFields) {
        final List<JsonNode> jsonNodes = new ArrayList<>();
        final Iterator<JsonNode> iter = in.elements();
        while (iter.hasNext()) {
            jsonNodes.add(iter.next());
        }
        Collections.sort(jsonNodes, new Comparator<JsonNode>() {

            @Override
            public int compare(JsonNode o1, JsonNode o2) {
                for (final OrderByField sortField : sortFields) {
                    JsonNode n1 = o1.get(sortField.name);
                    JsonNode n2 = o2.get(sortField.name);
                    if (n1 != null && n2 != null) {
                        if (n1 instanceof TextNode && n2 instanceof TextNode) {
                            final int comp = ((TextNode) n1).asText().compareTo(((TextNode) n2).asText());
                            if (comp != 0) {
                                return sortField.order == OrderByOrder.DESC ? comp * -1 : comp;
                            }
                        }
                        if (n1 instanceof NumericNode && n2 instanceof NumericNode) {
                            final int comp = (int) Double.compare(((NumericNode) n1).asDouble(), ((NumericNode) n2).asDouble());
                            if (comp != 0) {
                                return sortField.order == OrderByOrder.DESC ? comp * -1 : comp;
                            }

                        }
                        if (n1 instanceof BooleanNode && n2 instanceof BooleanNode) {
                            final int comp = Boolean.compare(((BooleanNode) n1).asBoolean(), ((BooleanNode) n2).asBoolean());
                            if (comp != 0) {
                                return sortField.order == OrderByOrder.DESC ? comp * -1 : comp;
                            }

                         }
                     }
				}
                return 0;
            }
        });
        return new ArrayNode(factory, jsonNodes);
    }

    private List<OrderByField> extractOrderByFields(Op_orderbyContext ctx) {
        final List<OrderByField> sortFields = new ArrayList<>();
        final int cnt = ctx.getChildCount();
        final OrderByOrder defaultOrder = OrderByOrder.ASC;
        OrderByOrder order = defaultOrder;
        for (int i = 0; i < cnt; i++) {
            ParseTree child = ctx.getChild(i);
            if (child.getPayload() instanceof CommonToken) {
                final CommonToken token = (CommonToken) child.getPayload();
                switch (token.getType()) {
                    case MappingExpressionParser.ID:
                        sortFields.add(new OrderByField(token.getText(), order));
                        order = defaultOrder;
                        break;
                    case MappingExpressionParser.GT:
                        order = OrderByOrder.ASC;
                        break;
                    case MappingExpressionParser.LT:
                        order = OrderByOrder.DESC;
                        break;
                }
            }
        }
        return sortFields;
    }

    class OrderByField {

        public final String name;
        public final OrderByOrder order;

        public OrderByField(String name, OrderByOrder order) {
            super();
            this.name = name;
            this.order = order;
        }
    }

    enum OrderByOrder {
            ASC, DESC
    }
}
