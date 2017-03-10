# ReciteWords

 Android Studio 翻译插件,可以将英文翻译为中文并**记录到生词本**提供查阅。

##功能
- 划词翻译
- 生词本保存翻译过后的历史记录

##使用方法

- Clone项目，获取根目录下的ReciteWords.jar。

- 打开Android Studio， Preferences -> Plugins -> Install plugin from disk -> 获取ReciteWords.jar安装并重启Android Studio。

- 选中代码，按下 Ctrl + Alt+Q(也可以自己设定)。即可翻译。效果如下:

![](./img/1.png)
- 你所翻译的单词会被记录在你**当前用户目录**下的ReciteWords.md文件中（如:C:\Users\Bolex\ReciteWords.md）。可以通过Markdown编辑器打开它进行学习。效果如下:

  ![](./img/2.png)

##修改快捷键

### 使用键盘快捷键触发
- Preferences -> Keymap -> 获取ReciteWords - > 右键 add Keyboard Shortcut. 输入你想要的快捷键即可。



##Thanks
- [有道翻译](http://fanyi.youdao.com/openapi?path=data-mode)
- [ECTranslation](https://github.com/Skykai521/ECTranslation)
- 该插件是基于ECTranslation扩展的。在原有的基础上添加单词保存的功能，方便后续将陌生单词记录下来学习。
