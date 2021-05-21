all: ./build/blocks.jar

run: ./build/blocks.jar
	cd ./build && java -jar ./blocks.jar

clean:
	rm -r -f ./build/classes/com/*
	rm -r -f ./build/classes/game/*
	rm -r -f ./build/classes/debug/*
	rm -r -f ./build/classes/util/*
	rm -r -f ./build/classes/graphics/*
	rm -r -f ./build/blocks.jar
	rm -r -f ./build/classes/assets/texture.zip

./build/classes/assets/texture.zip: texture/*
	mkdir -p ./build/classes/assets
	cd ./texture && zip -r ../build/classes/assets/texture.zip .

./build/classes/assets/doc.txt: ./doc.txt
	mkdir -p ./build/classes/assets
	cp ./doc.txt ./build/classes/assets/

./build/blocks.jar: com/ccz/blocks/* util/* graphics/* game/block/* game/item/* game/entity/* game/ui/* game/world/* game/socket/* game/GameSetting.java game/GlobalSetting.java debug/Log.java debug/ObjectInfo.java debug/script/* ./build/classes/assets/texture.zip ./build/classes/assets/doc.txt
	javac com/ccz/blocks/* util/* graphics/* game/block/* game/item/* game/entity/* game/ui/* game/world/* game/socket/* game/GameSetting.java game/GlobalSetting.java debug/Log.java debug/ObjectInfo.java debug/script/* -encoding utf-8 -d ./build/classes
	cd ./build/classes && jar cvfm ../blocks.jar MENIFEST.MF . > ../log.txt && cd ../..

price: ./build/blocks.jar
	mkdir -p ./build/classes/assets
	mkdir -p ./build/data/assets
	cd ./build && echo "init stat n __tmp__ start no_more_input " | java -jar ./blocks.jar
	cp ./build/data/assets/v2.dat ./build/classes/assets

release: ./build/blocks.jar
	mkdir -p ./build/release/data
	cp -r ./build/data/lv_* ./build/release/data
	cp ./build/blocks.jar ./build/release
	cp -r ./build/script ./build/release
	cp ./run.cmd ./build/release
	cp ./run.sh ./build/release
	cd ./build/release && zip -r ../Blocks.zip .
	rm -r ./build/release
	
