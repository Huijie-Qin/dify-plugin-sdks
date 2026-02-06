package com.dify.invocations.workflow_node;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import com.dify.entities.workflow_node.ModelConfig;
import com.dify.entities.workflow_node.NodeResponse;
import com.dify.entities.workflow_node.ParameterConfig;
import java.util.List;
import java.util.Map;

/**
 * 参数提取节点调用封装。
 */
public class ParameterExtractorNodeInvocation extends BackwardsInvocation<NodeResponse> {

    /**
     * 调用参数提取节点。
     */
    public NodeResponse invoke(
        List<ParameterConfig> parameters,
        ModelConfig model,
        String query,
        String instruction
    ) {
        Iterable<NodeResponse> response = backwardsInvoke(
            InvokeType.NodeParameterExtractor,
            NodeResponse.class,
            Map.of(
                "parameters", parameters,
                "model", model,
                "query", query,
                "instruction", instruction
            )
        );

        for (NodeResponse data : response) {
            return data;
        }

        throw new RuntimeException("No response from workflow node parameter extractor");
    }
}
