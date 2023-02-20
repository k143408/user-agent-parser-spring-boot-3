package org.useragent.parse;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class LiteDeviceResolver implements DeviceResolver {

  public static final String ANDROID = "android";
  public static final String MOBILE = "mobile";
  private final List<String> mobileUserAgentPrefixes = new ArrayList<String>();

  private final List<String> mobileUserAgentKeywords = new ArrayList<String>();

  private final List<String> tabletUserAgentKeywords = new ArrayList<String>();

  private final List<String> normalUserAgentKeywords = new ArrayList<String>();

  public LiteDeviceResolver() {
    init();
  }

  public LiteDeviceResolver(List<String> normalUserAgentKeywords) {
    this.normalUserAgentKeywords.addAll(normalUserAgentKeywords);
  }

  public Device resolveDevice(HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    if (userAgent == null) {
      return resolveFallback(request);
    }

    userAgent = userAgent.toLowerCase();
    if (isTablet(userAgent)) {
      return resolveWithPlatform(DeviceType.TABLET, getTabletPlatform(userAgent));
    }

    if (isMobile(request, userAgent)) {
      return resolveWithPlatform(DeviceType.MOBILE, getMobilePlatform(userAgent));
    }

    return resolveFallback(request);
  }

  private boolean isTablet(String userAgent) {
    if (userAgent.contains(ANDROID) && !userAgent.contains(MOBILE)) {
      return true;
    }
    if (userAgent.contains("ipad")) {
      return true;
    }
    if (userAgent.contains("silk") && !userAgent.contains(MOBILE)) {
      return true;
    }
    for (String keyword : tabletUserAgentKeywords) {
      if (userAgent.contains(keyword)) {
        return true;
      }
    }
    return false;
  }

  private DevicePlatform getTabletPlatform(String userAgent) {
    if (userAgent.contains(ANDROID)) {
      return DevicePlatform.ANDROID;
    }
    if (userAgent.contains("ipad")) {
      return DevicePlatform.IOS;
    }
    return DevicePlatform.UNKNOWN;
  }

  private boolean isMobile(HttpServletRequest request, String userAgent) {
    if (request.getHeader("x-wap-profile") != null || request.getHeader("Profile") != null) {
      return true;
    }
    if (userAgent.length() >= 4) {
      String prefix = userAgent.substring(0, 4).toLowerCase();
      if (mobileUserAgentPrefixes.contains(prefix)) {
        return true;
      }
    }
    String accept = request.getHeader("Accept");
    if (accept != null && accept.contains("wap")) {
      return true;
    }
    if (userAgent.contains(ANDROID)) {
      return true;
    }
    if (userAgent.contains("iphone") || userAgent.contains("ipod") || userAgent.contains("ipad")) {
      return true;
    }
    for (String keyword : mobileUserAgentKeywords) {
      if (userAgent.contains(keyword)) {
        return true;
      }
    }
    Enumeration<String> headers = request.getHeaderNames();
    while (headers.hasMoreElements()) {
      String header = headers.nextElement();
      if (header.contains("OperaMini")) {
        return true;
      }
    }
    return false;
  }

  private DevicePlatform getMobilePlatform(String userAgent) {
    if (userAgent.contains(ANDROID)) {
      return DevicePlatform.ANDROID;
    }
    if (userAgent.contains("iphone") || userAgent.contains("ipod") || userAgent.contains("ipad")) {
      return DevicePlatform.IOS;
    }
    return DevicePlatform.UNKNOWN;
  }

  // subclassing hooks

  /** Wrapper method for allow subclassing platform based resolution */
  protected Device resolveWithPlatform(DeviceType deviceType, DevicePlatform devicePlatform) {
    return LiteDevice.from(deviceType, devicePlatform);
  }

  /**
   * List of user agent prefixes that identify mobile devices. Used primarily to match by operator
   * or handset manufacturer.
   */
  protected List<String> getMobileUserAgentPrefixes() {
    return mobileUserAgentPrefixes;
  }

  /**
   * List of user agent keywords that identify mobile devices. Used primarily to match by mobile
   * platform or operating system.
   */
  protected List<String> getMobileUserAgentKeywords() {
    return mobileUserAgentKeywords;
  }

  /**
   * List of user agent keywords that identify tablet devices. Used primarily to match by tablet
   * platform or operating system.
   */
  protected List<String> getTabletUserAgentKeywords() {
    return tabletUserAgentKeywords;
  }

  /**
   * Initialize this device resolver implementation. Registers the known set of device signature
   * strings. Subclasses may override to register additional strings.
   */
  protected void init() {
    getMobileUserAgentPrefixes().addAll(Arrays.asList(KNOWN_MOBILE_USER_AGENT_PREFIXES));
    getMobileUserAgentKeywords().addAll(Arrays.asList(KNOWN_MOBILE_USER_AGENT_KEYWORDS));
    getTabletUserAgentKeywords().addAll(Arrays.asList(KNOWN_TABLET_USER_AGENT_KEYWORDS));
  }

  /**
   * Fallback called if no mobile device is matched by this resolver. The default implementation of
   * this method returns a "normal" {@link Device} that is neither mobile or a tablet. Subclasses
   * may override to try additional mobile or tablet device matching before falling back to a
   * "normal" device.
   */
  protected Device resolveFallback(HttpServletRequest request) {
    return LiteDevice.NORMAL_INSTANCE;
  }

  // internal helpers

  private static final String[] KNOWN_MOBILE_USER_AGENT_PREFIXES =
      new String[] {
        "w3c ", "w3c-", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq",
        "bird", "blac", "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco",
        "eric", "hipt", "htc_", "inno", "ipaq", "ipod", "jigs", "kddi", "keji",
        "leno", "lg-c", "lg-d", "lg-g", "lge-", "lg/u", "maui", "maxo", "midp",
        "mits", "mmef", "mobi", "mot-", "moto", "mwbp", "nec-", "newt", "noki",
        "palm", "pana", "pant", "phil", "play", "port", "prox", "qwap", "sage",
        "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-",
        "siem", "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-",
        "tosh", "tsm-", "upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi",
        "wapp", "wapr", "webc", "winw", "winw", "xda ", "xda-"
      };

  private static final String[] KNOWN_MOBILE_USER_AGENT_KEYWORDS =
      new String[] {
        "blackberry",
        "webos",
        "ipod",
        "lge vx",
        "midp",
        "maemo",
        "mmp",
          MOBILE,
        "netfront",
        "hiptop",
        "nintendo DS",
        "novarra",
        "openweb",
        "opera mobi",
        "opera mini",
        "palm",
        "psp",
        "phone",
        "smartphone",
        "symbian",
        "up.browser",
        "up.link",
        "wap",
        "windows ce"
      };

  private static final String[] KNOWN_TABLET_USER_AGENT_KEYWORDS =
      new String[] {"ipad", "playbook", "hp-tablet", "kindle"};
}
