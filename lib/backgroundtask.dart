import 'dart:async';

import 'package:flutter/services.dart';

class Backgroundtask {
  static const MethodChannel _channel = const MethodChannel('lince.com/backgroundtask');

  static Future<void> periodicTask() async {
    _channel.setMethodCallHandler((MethodCall call) async {
      print('metodo >> ${call.method}');
      print('args >> ${call.arguments}');
      print('Executed');
    });

    await _channel.invokeMethod('periodic');
  }
}
