import 'package:backgroundtask/backgroundtask.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:mailer/mailer.dart';
import 'package:mailer/smtp_server.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  await Backgroundtask.periodic(
    callback: taskEmail,
  );
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'example',
      debugShowCheckedModeBanner: false,
      home: MyPage(),
    );
  }
}

class MyPage extends StatefulWidget {
  @override
  _MyPageState createState() => _MyPageState();
}

class _MyPageState extends State<MyPage> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Center(child: CircularProgressIndicator()),
    );
  }
}

Future<void> taskEmail() async {
  print('executing task');
  
  final String username = 'flutter.background@gmail.com';
  final String password = '@123456789!';
  print('user $username and pass $password');

//  final SmtpServer smtpServer = gmail(username, password);
//
//  final Message message = Message()
//    ..from = Address(username, 'Background Test')
//    ..recipients.add('carlos.hjunior@lince.com.br')
//    ..subject = 'Test Dart Mailer library :: 😀 :: ${DateTime.now()}'
//    ..text = 'This is the plain text.\nThis is line 2 of the text part.';
//
//  try {
//    await send(message, smtpServer);
//  } catch (e, _) {
//    print('erro >> $e');
//  }
  print('end task');
}
