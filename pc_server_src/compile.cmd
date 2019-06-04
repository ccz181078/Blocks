@echo off
for /r .\Blocks\ %%i in (*.class) do echo Y|del %%i > nul
javac com\ccz\blocks\Main.java util\* graphics\* game\block\* game\item\* game\entity\* game\ui\* game\world\* game\socket\* game\GameSetting.java game\GlobalSetting.java debug\Log.java debug\ObjectInfo.java -encoding utf-8 -d .\Blocks
pause