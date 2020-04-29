import 'dart:async';
import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

class Backgroundtask {
  static const MethodChannel _channel = const MethodChannel('lince.com/backgroundtask');

  /// [callback] deve ser um metodo estático ou "top-level"
  static Future<bool> periodic({@required Function callback, WorkerConfiguration configuration}) async {
    assert(callback != null, 'task cannot be null');
    CallbackHandle handle = PluginUtilities.getCallbackHandle(callback);
    if (handle == null) {
      return false;
    }
    
    configuration ??= WorkerConfiguration();
    final bool result = await _channel.invokeMethod(
      'periodic',
      <dynamic>[
        handle.toRawHandle(),
        configuration._toMap(),
      ],
    );
    return result ?? false;
  }
}

/// [interval] Deve ser acima de 15 minutos
class WorkerConfiguration {
  const WorkerConfiguration({this.constraints, this.networkType, this.interval});
  
  Map<String, dynamic> _toMap() => <String, dynamic>{
    'constraints': constraints?.map((Constraint c) => c.index)?.toList() ?? null,
    'networkType': networkType?.toString() ?? null,
    'interval': interval?.inSeconds ?? Duration(minutes: 15).inSeconds,
  };
  
  final List<Constraint> constraints;
  final NetworkType networkType;
  final Duration interval;
}

/// Doc: https://developer.android.com/reference/androidx/work/Constraints
enum Constraint {
  BATTERY_NOT_LOW,
  CHARGING,
  DEVICE_IDLE,
  STORAGE_NOT_LOW,
}

/// Doc: https://developer.android.com/reference/androidx/work/NetworkType
enum NetworkType {
  NOT_REQUIRED,
  CONNECTED,
  UNMETERED,
  NOT_ROAMING,
  METERED,
}
