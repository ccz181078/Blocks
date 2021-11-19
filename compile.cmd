javac com/ccz/blocks/* util/* graphics/* game/block/* game/item/* game/entity/* game/ui/* game/world/* game/socket/* game/GameSetting.java game/GlobalSetting.java debug/Log.java debug/ObjectInfo.java debug/script/* -encoding utf-8 -d ./build/classes
cd ./build/classes && jar cvfm ../blocks.jar MENIFEST.MF . > ../log.txt && cd ../..
pause
