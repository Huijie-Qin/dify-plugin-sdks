package com.dify.core.runtime;

import com.dify.invocations.app.ChatAppInvocation;
import com.dify.invocations.app.CompletionAppInvocation;
import com.dify.invocations.app.FetchAppInvocation;
import com.dify.invocations.app.WorkflowAppInvocation;
import java.util.Map;
import lombok.Getter;

/**
 * 应用相关反向调用集合。
 */
@Getter
public class AppInvocations {

    private final ChatAppInvocation chat;
    private final CompletionAppInvocation completion;
    private final WorkflowAppInvocation workflow;
    private final FetchAppInvocation fetchAppInvocation;

    public AppInvocations(Session session) {
        this.chat = new ChatAppInvocation(session);
        this.completion = new CompletionAppInvocation(session);
        this.workflow = new WorkflowAppInvocation(session);
        this.fetchAppInvocation = new FetchAppInvocation(session);
    }

    /**
     * 获取应用信息。
     */
    public Map<String, Object> fetchApp(String appId) {
        return fetchAppInvocation.get(appId);
    }
}
