package com.dify.dify_plugin.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 参数选项对象。
 */
@Data
@AllArgsConstructor
public class ParameterOption {
    /** 参数值。 */
    private String value;
    /** 展示用国际化文案。 */
    private I18nObject label;
}
