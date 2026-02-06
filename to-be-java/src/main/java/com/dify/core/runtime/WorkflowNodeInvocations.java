package com.dify.core.runtime;

import com.dify.invocations.workflow_node.ParameterExtractorNodeInvocation;
import com.dify.invocations.workflow_node.QuestionClassifierNodeInvocation;
import lombok.Getter;

/**
 * 工作流节点相关反向调用集合。
 */
@Getter
public class WorkflowNodeInvocations {

    private final QuestionClassifierNodeInvocation questionClassifier;
    private final ParameterExtractorNodeInvocation parameterExtractor;

    public WorkflowNodeInvocations(Session session) {
        this.questionClassifier = new QuestionClassifierNodeInvocation(session);
        this.parameterExtractor = new ParameterExtractorNodeInvocation(session);
    }
}
