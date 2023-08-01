# ItemChecker

-settings内のYamlファイルの形式-
[名前]:
  checknbt:
  └NBTに含まれている文字列
  useregularnbt: true
  └NBT判別に正規表現を使用するか否か(必須ではない)
    checklist:
      [名前]:
        containedtext: testitem
        ├アイテム名に含まれている文字列(無い場合NBTのみで実行)
        └containedtextが複数無い場合は一つだけ実行されます
        useregular: true
        └アイテム名判別に正規表現を使用するか否か(必須ではない)
        consolecommand: /kill %player%
        └上記を両方満たした場合に実行されるコマンド
      [名前2]:
        containedtext: testitem2
        etc...

※出来る限り文字列は"(ダブルクォーテーション)で囲んでください

-configファイルで使用できる設定-
DisableChestName:
└置き換え対象のアイテムを置き換え対象から除外するためのチェスト名です。
 この設定がない場合は機能しなく、ある場合は設定した名前のチェストに置き換え対象アイテムを入れると
 NBTが付与され自動置き換えされなくなります

-実行コマンドに使用可能な変数-
%player%: アイテム所持者
%amount%: 置き換え前アイテム個数

-使用可能コマンド-
/ic reload
└Yamlファイルのリロード
/ic show
└読み込まれているリストの表示