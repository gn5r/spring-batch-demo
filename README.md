# batch-demo

[気象庁|過去の気象データ・ダウンロード](https://www.data.jma.go.jp/gmd/risk/obsdl/index.php)でダウンロードしたCSVファイルを加工したサンプルデータから以下のデータをテーブルに登録するバッチサンプルです。
サンプルデータは東京の2024/10/01〜2024/11/01が記載されています。

- 都道府県
- 日付
- 最高気温
- 最低気温
- 平均気温

## 実行の仕方
### Mavenコマンド

```
mvn clean spring-boot:run -Dspring.run.arguments='--batch.execute.target=MeteorologyDataImportRunner'
```

### jarコマンド

```
java -jar target/batch-demo-1.0.0-SNAPSHOT.jar --batch.execute.target=MeteorologyDataImportRunner
```