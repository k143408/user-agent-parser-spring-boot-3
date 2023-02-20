/*
 * Copyright 2010-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.useragent.parse;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;

/**
 * Static helper for accessing request-scoped Device values.
 *
 */
public final class DeviceUtils {

  public static final String CURRENT_DEVICE_ATTRIBUTE = "currentDevice";

  private DeviceUtils() {}

  public static Device getCurrentDevice(HttpServletRequest request) {
    return (Device) request.getAttribute(CURRENT_DEVICE_ATTRIBUTE);
  }

  public static Device getRequiredCurrentDevice(HttpServletRequest request) {
    Device device = getCurrentDevice(request);
    if (device == null) {
      throw new IllegalStateException(
          "No currenet device is set in this request and one is required - have you configured a"
              + " DeviceResolvingHandlerInterceptor?");
    }
    return device;
  }

  public static Device getCurrentDevice(RequestAttributes attributes) {
    return (Device)
        attributes.getAttribute(CURRENT_DEVICE_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
  }
}
