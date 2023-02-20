package org.useragent.parse;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LiteDeviceTest {

  @Test
  void testIsNormal() {
    assertTrue(LiteDevice.NORMAL_INSTANCE.isNormal());
    assertFalse(LiteDevice.MOBILE_INSTANCE.isNormal());
    assertFalse(LiteDevice.TABLET_INSTANCE.isNormal());
  }

  @Test
  void testIsMobile() {
    assertFalse(LiteDevice.NORMAL_INSTANCE.isMobile());
    assertTrue(LiteDevice.MOBILE_INSTANCE.isMobile());
    assertFalse(LiteDevice.TABLET_INSTANCE.isMobile());
  }

  @Test
  void testIsTablet() {
    assertFalse(LiteDevice.NORMAL_INSTANCE.isTablet());
    assertFalse(LiteDevice.MOBILE_INSTANCE.isTablet());
    assertTrue(LiteDevice.TABLET_INSTANCE.isTablet());
  }

  @Test
  void testGetDevicePlatform() {
    assertTrue(LiteDevice.NORMAL_INSTANCE.getDevicePlatform() == DevicePlatform.UNKNOWN);
    assertTrue(LiteDevice.MOBILE_INSTANCE.getDevicePlatform() == DevicePlatform.UNKNOWN);
    assertTrue(LiteDevice.TABLET_INSTANCE.getDevicePlatform() == DevicePlatform.UNKNOWN);
  }

  @Test
  void testGetDeviceType() {
    assertTrue(LiteDevice.NORMAL_INSTANCE.getDeviceType() == DeviceType.NORMAL);
    assertTrue(LiteDevice.MOBILE_INSTANCE.getDeviceType() == DeviceType.MOBILE);
    assertTrue(LiteDevice.TABLET_INSTANCE.getDeviceType() == DeviceType.TABLET);
  }
}
