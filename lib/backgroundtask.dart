import 'dart:async';
import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

class Backgroundtask {
  static const MethodChannel _channel = const MethodChannel('lince.com/backgroundtask');

  static Future<bool> periodic({@required Function callback}) async {
    assert(callback != null, 'task cannot be null');

    CallbackHandle handle = PluginUtilities.getCallbackHandle(callback);
    if (handle == null) {
      return false;
    }

    final bool result = await _channel.invokeMethod(
      'periodic',
      <dynamic>[
        handle.toRawHandle(),
      ],
    );
    return result ?? false;
  }
}
