package com.dify.dify_plugin.core.entities.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应 Python 的 request.py，包含插件调用相关的枚举与请求结构。
 * <p>
 * 说明：这里使用包级可见类承载多个请求结构，避免拆分过多文件。
 * </p>
 */
enum PluginInvokeType {
    Tool,
    Model,
    Endpoint,
    Agent,
    Trigger,
    OAuth,
    Datasource,
    DynamicParameter
}

enum AgentActions {
    InvokeAgentStrategy
}

enum TriggerActions {
    InvokeTriggerEvent,
    ValidateProviderCredentials,
    DispatchTriggerEvent,
    SubscribeTrigger,
    UnsubscribeTrigger,
    RefreshTrigger
}

enum ToolActions {
    ValidateCredentials,
    InvokeTool,
    GetToolRuntimeParameters
}

enum ModelActions {
    ValidateProviderCredentials,
    ValidateModelCredentials,
    InvokeLLM,
    GetLLMNumTokens,
    InvokeTextEmbedding,
    InvokeMultimodalEmbedding,
    GetTextEmbeddingNumTokens,
    InvokeRerank,
    InvokeMultimodalRerank,
    InvokeTTS,
    GetTTSVoices,
    InvokeSpeech2Text,
    InvokeModeration,
    GetAIModelSchemas
}

enum EndpointActions {
    InvokeEndpoint
}

enum OAuthActions {
    GetAuthorizationUrl,
    GetCredentials,
    RefreshCredentials
}

enum DatasourceActions {
    ValidateCredentials,
    InvokeWebsiteDatasourceGetCrawl,
    InvokeOnlineDocumentDatasourceGetPages,
    InvokeOnlineDocumentDatasourceGetPageContent,
    InvokeOnlineDriveBrowseFiles,
    InvokeOnlineDriveDownloadFile
}

enum DynamicParameterActions {
    FetchParameterOptions
}

/**
 * 基础的插件访问请求。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class PluginAccessRequest {
    private PluginInvokeType type;
    private String userId;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ToolInvokeRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Tool;
    private ToolActions action = ToolActions.InvokeTool;
    private String provider;
    private String tool;
    private Map<String, Object> credentials = new HashMap<>();
    private Object credentialType;
    private Map<String, Object> toolParameters = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class AgentInvokeRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Agent;
    private AgentActions action = AgentActions.InvokeAgentStrategy;
    private String agentStrategyProvider;
    private String agentStrategy;
    private Map<String, Object> agentStrategyParams = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ToolValidateCredentialsRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Tool;
    private ToolActions action = ToolActions.ValidateCredentials;
    private String provider;
    private Map<String, Object> credentials = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ToolGetRuntimeParametersRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Tool;
    private ToolActions action = ToolActions.GetToolRuntimeParameters;
    private String provider;
    private String tool;
    private Map<String, Object> credentials = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class PluginAccessModelRequest {
    private PluginInvokeType type = PluginInvokeType.Model;
    private String userId;
    private String provider;
    private Object modelType;
    private String model;
    private Map<String, Object> credentials = new HashMap<>();
}

/**
 * 提示消息混入结构，用于统一处理 promptMessages。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class PromptMessageMixin {
    private List<Object> promptMessages = new ArrayList<>();

    /**
     * 将输入的消息列表转换为对应的 PromptMessage 类型。
     * 该逻辑对应 Python 的 field_validator。
     *
     * @param raw 原始消息列表
     * @return 转换后的列表
     */
    public List<Object> convertPromptMessages(List<Object> raw) {
        if (raw == null) {
            throw new IllegalArgumentException("prompt_messages must be a list");
        }
        // 逐条遍历并按 role 进行类型转换，这里保留结构并使用 Map 做中间态。
        List<Object> converted = new ArrayList<>();
        for (Object item : raw) {
            converted.add(item);
        }
        return converted;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelInvokeLLMRequest extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.InvokeLLM;
    private Map<String, Object> modelParameters = new HashMap<>();
    private List<String> stop;
    private List<Object> tools;
    private boolean stream = true;
    private List<Object> promptMessages = new ArrayList<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelGetLLMNumTokens extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.GetLLMNumTokens;
    private List<Object> tools;
    private List<Object> promptMessages = new ArrayList<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelInvokeTextEmbeddingRequest extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.InvokeTextEmbedding;
    private Object inputType;
    private Object textEmbeddingInput;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelInvokeMultimodalEmbeddingRequest extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.InvokeMultimodalEmbedding;
    private List<Object> inputs = new ArrayList<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelGetTextEmbeddingNumTokens extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.GetTextEmbeddingNumTokens;
    private Object inputType;
    private Object textEmbeddingInput;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelInvokeRerankRequest extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.InvokeRerank;
    private List<String> documents = new ArrayList<>();
    private String query;
    private Map<String, Object> modelParameters = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelInvokeMultimodalRerankRequest extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.InvokeMultimodalRerank;
    private List<Object> documents = new ArrayList<>();
    private Object query;
    private Map<String, Object> modelParameters = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelInvokeTTSRequest extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.InvokeTTS;
    private String text;
    private Map<String, Object> modelParameters = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelGetTTSVoices extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.GetTTSVoices;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelInvokeSpeech2TextRequest extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.InvokeSpeech2Text;
    private String file;
    private Map<String, Object> modelParameters = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelInvokeModerationRequest extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.InvokeModeration;
    private String text;
    private Map<String, Object> modelParameters = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelGetAIModelSchemas extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.GetAIModelSchemas;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelValidateProviderCredentialsRequest extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.ValidateProviderCredentials;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ModelValidateModelCredentialsRequest extends PluginAccessModelRequest {
    private ModelActions action = ModelActions.ValidateModelCredentials;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class EndpointInvokeRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Endpoint;
    private EndpointActions action = EndpointActions.InvokeEndpoint;
    private String endpointId;
    private Map<String, Object> params = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class OAuthGetAuthorizationUrlRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.OAuth;
    private OAuthActions action = OAuthActions.GetAuthorizationUrl;
    private String provider;
    private Map<String, Object> params = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class OAuthGetCredentialsRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.OAuth;
    private OAuthActions action = OAuthActions.GetCredentials;
    private String provider;
    private Map<String, Object> params = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class OAuthRefreshCredentialsRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.OAuth;
    private OAuthActions action = OAuthActions.RefreshCredentials;
    private String provider;
    private Map<String, Object> params = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class DatasourceValidateCredentialsRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Datasource;
    private DatasourceActions action = DatasourceActions.ValidateCredentials;
    private String provider;
    private Map<String, Object> credentials = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class DatasourceCrawlWebsiteRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Datasource;
    private DatasourceActions action = DatasourceActions.InvokeWebsiteDatasourceGetCrawl;
    private String provider;
    private Map<String, Object> credentials = new HashMap<>();
    private Map<String, Object> params = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class DatasourceGetPagesRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Datasource;
    private DatasourceActions action = DatasourceActions.InvokeOnlineDocumentDatasourceGetPages;
    private String provider;
    private Map<String, Object> credentials = new HashMap<>();
    private Object request;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class DatasourceGetPageContentRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Datasource;
    private DatasourceActions action = DatasourceActions.InvokeOnlineDocumentDatasourceGetPageContent;
    private String provider;
    private Map<String, Object> credentials = new HashMap<>();
    private Object request;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class DatasourceOnlineDriveBrowseFilesRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Datasource;
    private DatasourceActions action = DatasourceActions.InvokeOnlineDriveBrowseFiles;
    private String provider;
    private Map<String, Object> credentials = new HashMap<>();
    private Object request;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class DatasourceOnlineDriveDownloadFileRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Datasource;
    private DatasourceActions action = DatasourceActions.InvokeOnlineDriveDownloadFile;
    private String provider;
    private Map<String, Object> credentials = new HashMap<>();
    private Object request;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class DynamicParameterFetchParameterOptionsRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.DynamicParameter;
    private DynamicParameterActions action = DynamicParameterActions.FetchParameterOptions;
    private String provider;
    private String tool;
    private String parameter;
    private Map<String, Object> credentials = new HashMap<>();
    private Map<String, Object> params = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TriggerInvokeEventRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Trigger;
    private TriggerActions action = TriggerActions.InvokeTriggerEvent;
    private String provider;
    private String trigger;
    private Map<String, Object> credentials = new HashMap<>();
    private Object event;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TriggerInvokeEventResponse {
    private Object variables;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TriggerDispatchEventRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Trigger;
    private TriggerActions action = TriggerActions.DispatchTriggerEvent;
    private String provider;
    private Map<String, Object> credentials = new HashMap<>();
    private Object event;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TriggerDispatchResponse {
    private Object dispatch;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TriggerValidateProviderCredentialsRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Trigger;
    private TriggerActions action = TriggerActions.ValidateProviderCredentials;
    private String provider;
    private Map<String, Object> credentials = new HashMap<>();
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TriggerSubscribeRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Trigger;
    private TriggerActions action = TriggerActions.SubscribeTrigger;
    private String provider;
    private String trigger;
    private Map<String, Object> credentials = new HashMap<>();
    private Object subscription;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TriggerSubscriptionResponse {
    private Object subscription;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TriggerUnsubscribeRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Trigger;
    private TriggerActions action = TriggerActions.UnsubscribeTrigger;
    private String provider;
    private String trigger;
    private Map<String, Object> credentials = new HashMap<>();
    private Object subscription;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TriggerUnsubscribeResponse {
    private Object result;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TriggerRefreshRequest extends PluginAccessRequest {
    private PluginInvokeType type = PluginInvokeType.Trigger;
    private TriggerActions action = TriggerActions.RefreshTrigger;
    private String provider;
    private String trigger;
    private Map<String, Object> credentials = new HashMap<>();
    private Object subscription;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TriggerRefreshResponse {
    private Object subscription;
}
