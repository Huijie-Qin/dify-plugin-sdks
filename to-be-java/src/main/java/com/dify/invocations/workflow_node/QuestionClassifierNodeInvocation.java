package com.dify.invocations.workflow_node;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import com.dify.entities.workflow_node.ClassConfig;
import com.dify.entities.workflow_node.ModelConfig;
import com.dify.entities.workflow_node.NodeResponse;
import java.util.List;
import java.util.Map;

/**
 * 问题分类节点调用封装。
 */
public class QuestionClassifierNodeInvocation extends BackwardsInvocation<NodeResponse> {

    /**
     * 调用问题分类节点。
     */
    public NodeResponse invoke(
        List<ClassConfig> classes,
        ModelConfig model,
        String query,
        String instruction
    ) {
        Iterable<NodeResponse> response = backwardsInvoke(
            InvokeType.NodeQuestionClassifier,
            NodeResponse.class,
            Map.of(
                "classes", classes,
                "model", model,
                "query", query,
                "instruction", instruction
            )
        );

        for (NodeResponse data : response) {
            return data;
        }

        throw new RuntimeException("No response from workflow node question classifier");
    }
}
