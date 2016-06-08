//============================================================
//
//  main.js
//
//============================================================

//============================================================

//jQuery Functions

$(document).ready(function() {
    var arrButtons = {};
    var Yes = $('#dialog_yes').val();
    var No = $('#dialog_no').val();


//  checkbox allcheck -012 -018 only ===== ===== ===== ===== ===== =====
//
  $("body").on('click',function(){

    //セレクタを変数に格納
    var $tgt_parent = $("input.allchecker1");////親チェックボックス
    var $tgt_child = $("input.allCheckItems");////子チェックボックス(複数)

    //親チェックボックス動作
    $tgt_parent.on('click',function(){
      $(this).parents("div.parentCheckBoxAll").find('td input.allCheckItems').prop('checked', this.checked);
    });

    //子チェックボックス動作
    $tgt_child.on('click',function(){

      var checkNum = $(this).parents('table.allcheckTB').find('td input.allCheckItems:checked').length;
      var listNum = $(this).parents('table.allcheckTB').find('td input.allCheckItems').length;

      if(checkNum < listNum){
        $(this).parents("div.parentCheckBoxAll").find("input.allchecker1:checkbox").prop('checked','');
      }
      if(checkNum == listNum){
        $(this).parents("div.parentCheckBoxAll").find("input.allchecker1:checkbox").prop('checked','checked');
      }

    });

  });

  //親チェックボックス動作1
  $(function(){
    var $tgt_parent = $("input.allchecker1");

    $tgt_parent.click(function(){
      $(this).parents("div.parentCheckBoxAll").find('td input.allCheckItems').prop('checked', this.checked);
    });
  });



//  checkbox tr clickable and all check-012 -018 only ===== ===== ===== ===== ===== =====
//
  //mouseenter(hover) addClass, mouseleave removeClass, click function
  $("body").on('mouseenter', 'table.allcheckTB tr',function(){
    $(this).addClass('hover_tr');
  }).on('mouseleave', 'table.allcheckTB tr',function(){
    $(this).removeClass('hover_tr');
  }).on('click', 'table.allcheckTB tr', function(evt){

    var $t = $(this);
    var chk = $t.find(':checkbox')[0];

    if(evt.target != chk) {
      chk.checked = !chk.checked;
    }

    var checkNum = $('table.allcheckTB').find('td input.allCheckItems:checked').length;
    var listNum = $('table.allcheckTB').find('td input.allCheckItems').length;

    if(checkNum == listNum){
        $("div.parentCheckBoxAll").find("input.allchecker1:checkbox").prop('checked','checked');
    } else if(checkNum < listNum){
      $("div.parentCheckBoxAll").find("input.allchecker1:checkbox").prop('checked','');
    }

  });



// radiobutton tr clickable radio -005 -023 only ===== ===== ===== ===== ===== =====
//
  //mouseenter(hover) addClass, mouseleave removeClass, click function
  $("body").on('mouseenter', 'table.clickable-rows tr',function(){
    $(this).addClass('hover_tr');
  }).on('mouseleave', 'table.clickable-rows tr',function(){
    $(this).removeClass('hover_tr');
  }).on('click', 'table.clickable-rows tr', function(evt){

    var $t = $(this);
    var chkR = $t.find('input[type="radio"]').val();
    $t.find('input[type="radio"]').each(function(){
        if (!$(this).is(":disabled")) {
            $(this).prop("checked", true);
        }
    });

  });



//  checkbox checked disable textfield ===== ===== ===== ===== ===== =====
//
  //onoff check change func -008 -021 use
  $.fn.extend({

    toggle_avail: function() {
      return this.each(function() {

        if (this.disabled == true) {
          this.disabled = false;
        }
        else {
          this.disabled = true;
        }

      });
    }

  });


//    //onoff change elem
//    $(".perentCbox1").click(function() {
//      $(".perentCbox1Items").toggle_avail();
//    });

    //onoff change loginID -021 only
    $(".perentCboxLoginID").click(function() {
      $(".perentCboxLoginIDItems").toggle_avail();
    });

    //onoff change password -021 only
    $(".perentCboxPass").click(function() {
      $(".perentCboxPassItems").toggle_avail();
    });

    //perentAlls2 -021　only
    //
    $(".fn-perentAlls2").click(function(){

      var num = $(".fn-perentAlls2").index(this);

      if(num == 0){
        $(".fn-perentAllItems").prop("disabled", true);
        $(".fn-perentAllItemsR").prop("disabled", false);
      } else {
        $(".fn-perentAllItems").prop("disabled", false);
        $(".fn-perentAllItemsR").prop("disabled", true);
      }

    });





//fn-perentAlls -008　only 転送設定 ===== ===== ===== ===== ===== =====
//
    $(".fn-perentAlls").click(function(){

      var num = $(".fn-perentAlls").index(this);//クリックするラジオボタンのindex番号

      if(num == 0){//0 転送するボタン

        $(".fn-perentAllItems").prop("disabled", false);//すべてアクティブに

        //fn-callTimeTg01 無条件転送がチェックオンであった場合の基本設定
        if($(".fn-callTimeTg01:checked").length > 0) {
          $(".fn-callTimeTg02").prop('disabled', true);
          $(".fn-callTimeTg03").prop('disabled', true);
          $(".fn-callTimeTg04").prop('disabled', true);
          $(".fn-callTime:text").prop('disabled', true);
        }

        //無応答転送03 がチェックオフであった場合は呼び出し時間を非アクティブ
        if($(".fn-callTimeTg03:checked").length == 0) {
          $(".fn-callTime:text").prop('disabled', true);
        }

        //話中時転送02 か 不通時転送04 がチェックオンの場合は呼び出し時間を非アクティブ
        if($(".fn-callTimeTg02:checked").length > 0 || $(".fn-callTimeTg04:checked").length > 0) {
          $(".fn-callTime:text").prop('disabled', true);
        }

        //fn-callTimeTg03  無応答転送03 がチェックオンであった場合は呼び出し時間アクティブ
        if($(".fn-callTimeTg03:checked").length > 0) {
          $(".fn-callTime:text").prop('disabled', false);
        }

        //fn-callTimeTg01  無条件転送がチェックオンであった場合がオンであった場合は呼び出し時間非アクティブ
        if($(".fn-callTimeTg01:checked").length > 0) {
          $(".fn-callTime:text").prop('disabled', true);
        }

      } else if(num == 1){//1 転送しないボタン

        $(".fn-perentAllItems").prop("disabled", true);//すべて非アクティブに
        //$(".fn-perentAllItems:checkbox").prop("checked", false);//チェックもオフ

      }

    });


//  //fn-callTimeItems -008　only 転送動作　設定2 条件に応じた呼び出した転送動作項目および呼び出し時間のオンオフ
//
//  //fn-callTimeTg01  無条件転送
//  //fn-callTimeTg02  話中時転送
//  //fn-callTimeTg03  無応答転送
//  //fn-callTimeTg04  不通時転送
//  //fn-callTime      呼び出し時間　fn-callTimeTg01～04をクリックした時の状態により、呼び出し時間テキストボックスのアクティブ・非アクティブを制御

    // fn-callTimeTg01  無条件転送 チェックボックスの動作
    $(".fn-callTimeTg01").click(function() {
      if($(".fn-callTimeTg01:checked").length > 0) {//無条件転送01 チェックオンにする

        //チェックをオンにしたら 話中時転送02 無応答転送03 不通時転送04 各チェックボックスと呼び出し時間を非アクティブにする
        $(".fn-callTimeTg02").prop('disabled', true);
        $(".fn-callTimeTg03").prop('disabled', true);
        $(".fn-callTimeTg04").prop('disabled', true);
        $(".fn-callTime:text").prop('disabled', true);

      } else if($(".fn-callTimeTg01:checked").length == 0) {//無条件転送01 チェックオフにする

        //オフ時の基本動作(非アクティブをアクティブにする)
        $(".fn-callTimeTg02").prop('disabled', false);
        $(".fn-callTimeTg03").prop('disabled', false);
        $(".fn-callTimeTg04").prop('disabled', false);
        $(".fn-callTime:text").prop('disabled', false);

        //無応答転送03 がチェックオンの場合は呼び出し時間をアクティブ
        if($(".fn-callTimeTg03:checked").length == 0) {
          $(".fn-callTime:text").prop('disabled', true);
        }
        //話中時転送02 か 不通時転送04 がチェックオンの場合は呼び出し時間を非アクティブ
        if($(".fn-callTimeTg02:checked").length > 0 || $(".fn-callTimeTg04:checked").length > 0) {
          $(".fn-callTime:text").prop('disabled', true);
        }
        //無応答転送03 がチェックオンの場合は呼び出し時間をアクティブ
        if($(".fn-callTimeTg03:checked").length > 0) {
          $(".fn-callTime:text").prop('disabled', false);
        }

      } else {

      }

    });


    // fn-callTimeTg02  話中時転送 チェックボックスの動作
    $(".fn-callTimeTg02").click(function() {
      if($(".fn-callTimeTg03:checked").length > 0) {
        $(".fn-callTime:text").prop('disabled', false);
      } else if($(".fn-callTimeTg04:checked").length > 0) {
        $(".fn-callTime:text").prop('disabled', true);
      } else if($(".fn-callTimeTg02:checked").length > 0) {
        $(".fn-callTime:text").prop('disabled', true);
      } else if($(".fn-callTimeTg03:checked").length == 0) {
        $(".fn-callTime:text").prop('disabled', true);
      } else {
        $(".fn-callTime:text").prop('disabled', false);
      }
    });


    // fn-callTimeTg03  無応答転送 チェックボックスの動作
    $(".fn-callTimeTg03").click(function() {
      if($(".fn-callTimeTg01:checked").length > 0) {
        $(".fn-callTime:text").prop('disabled', true);
      } else if($(".fn-callTimeTg03:checked").length > 0) {
        $(".fn-callTime:text").prop('disabled', false);
      } else if($(".fn-callTimeTg03:checked").length == 0) {
        $(".fn-callTime:text").prop('disabled', true);
      } else if ($(".fn-callTimeTg02:checked").length > 0) {
        $(".fn-callTime:text").prop('disabled', true);
      } else if ($(".fn-callTimeTg04:checked").length > 0) {
        $(".fn-callTime:text").prop('disabled', true);
      }
    });


    // fn-callTimeTg04  不通時転送 チェックボックスの動作
    $(".fn-callTimeTg04").click(function() {
      if($(".fn-callTimeTg03:checked").length > 0) {
        $(".fn-callTime:text").prop('disabled', false);
      } else if($(".fn-callTimeTg02:checked").length > 0) {
        $(".fn-callTime:text").prop('disabled', true);
      } else if($(".fn-callTimeTg04:checked").length > 0) {
        $(".fn-callTime:text").prop('disabled', true);
      } else if($(".fn-callTimeTg03:checked").length == 0) {
        $(".fn-callTime:text").prop('disabled', true);
      } else {
        $(".fn-callTime:text").prop('disabled', false);
      }
    });






//  迷惑電話お断り：設定情報入力　各種設定等 -012  only ===== ===== ===== ===== ===== =====
//
    $(function(){

      var maxN012 = 30;//項目の最大数
      var listItemNum012 = $('#target012-main.allcheckTB').find('td input.allCheckItems').length;//項目の個数(チェックボックスの個数で判別)
      $("p.searchResultHitCount span.cnt012").text( listItemNum012 + "件/" );//件数表示書き換え



      //012初期チェック
      if( listItemNum012 >= maxN012 ) {
        //最大値なら無効(追加ボタン)
        $("#addbtn012-1").addClass('ui-button-disabled').addClass('ui-state-disabled').prop('disabled', true);
        //有効(削除ボタン)
        $("#delbtn012-1").removeClass('ui-button-disabled').removeClass('ui-state-disabled').prop('disabled', false);
        $("#target012-1 input.allchecker1").prop('checked', false).css('visibility', 'visible');
      } else if ( listItemNum012 <= 0 ) {
        //0以下であっても最大ではないので有効(追加ボタン)
        $("#addbtn012-1").removeClass('ui-button-disabled').removeClass('ui-state-disabled').prop('disabled', false);
        //0以下なら無効(削除ボタン)
        $("#delbtn012-1").addClass('ui-button-disabled').addClass('ui-state-disabled').prop('disabled', true);
        $("#target012-1 input.allchecker1").prop('checked', false).css('visibility', 'hidden');
      } else {
        //最大ではなければ有効(追加ボタン)
        $("#addbtn012-1").removeClass('ui-button-disabled').removeClass('ui-state-disabled').prop('disabled', false);
        //0以下ではないので有効(削除ボタン)
        $("#delbtn012-1").removeClass('ui-button-disabled').removeClass('ui-state-disabled').prop('disabled', false);
        $("#target012-1 input.allchecker1").prop('checked', false).css('visibility', 'visible');
      };



      ////追加ボタンの動作 ===== =====
      $("#addbtn012-1").click(function() {

        $(this).blur();
        var addNum = $("#addnum012-1:text").val();//フォームに入力された値(＝テーブルに追加する値)
        var listItemNum012 = $("#target012-main.allcheckTB").find("td input.allCheckItems").length;//項目の個数
        var setN = listItemNum012 + 1;//チェックボックスのIDやvalue等に追加するユニークな数字用
        var setNtbi = setN + 4;//tabindexのみ異なる数になるため別途設定

        if( listItemNum012 >= maxN012 ) {

          //すでに最大値なら無効(追加ボタン)
          $("#addbtn012-1").addClass('ui-button-disabled').addClass('ui-state-disabled').prop('disabled', true);

        } else if ( $("#addnum012-1").val() != "" ) {////追加処理ここから =====

          ////対象テーブルの一番上に、入力された値をもとにした行を追加
          //addNum = addNum.split('-').join('');//値、行の追加前処理1　ハイフンが含まれていたら除去
          //$("#addnum012-1:text").val(addNum);//上記の値をフォームにも反映

          $("#target012-main tbody").prepend("<tr>" + "<td class='wxSmall'><input type='checkbox' name='checkbox" + (setN) +"' value='checkbox1_" + (setN) +"_value' id='checkbox1_" + (setN) +"' class='allCheckItems' tabindex='" + (setNtbi) +"'></td>" + "<td class='wLarge alL'>" + (addNum) + "</td>" + "<td></td>" + "</tr>");
          $("#addnum012-1:text").val("");

          ////項目数追加に伴ってテーブル1行ごとの色を付けなおし
          $("#target012-main tr").removeClass("even-row");
          $("#target012-main tr:odd").addClass("even-row");

          var listItemNum012 = $("#target012-main.allcheckTB").find("td input.allCheckItems").length;//項目の個数　再度取得し再設定
          $("p.searchResultHitCount span.cnt012").text( listItemNum012 + "件/" );//件数表示書き換え

          $("#target012-1 input.allchecker1").prop('checked', false);


          //追加ボタンを押したあと
          if( listItemNum012 >= maxN012 ) {
            //最大値なら無効(追加ボタン)
            $("#addbtn012-1").addClass('ui-button-disabled').addClass('ui-state-disabled').removeClass('ui-state-hover').prop('disabled', true);
            //有効(削除ボタン)
            $("#delbtn012-1").removeClass('ui-button-disabled').removeClass('ui-state-disabled').prop('disabled', false).button("refresh");
            $("#target012-1 input.allchecker1").prop('checked', false).css('visibility', 'visible');
          } else if ( listItemNum012 <= 0 ) {
            //0以下であっても最大ではないので有効(追加ボタン)
            $("#addbtn012-1").removeClass('ui-button-disabled').removeClass('ui-state-disabled').prop('disabled', false).button("refresh");
            //0以下なら無効(削除ボタン)
            $("#delbtn012-1").addClass('ui-button-disabled').addClass('ui-state-disabled').removeClass('ui-state-hover').prop('disabled', true);
            $("#target012-1 input.allchecker1").prop('checked', false).css('visibility', 'hidden');
          } else {
            //最大ではなければ有効(追加ボタン)
            $("#addbtn012-1").removeClass('ui-button-disabled').removeClass('ui-state-disabled').prop('disabled', false).button("refresh");
            //0以下ではないので有効(削除ボタン)
            $("#delbtn012-1").removeClass('ui-button-disabled').removeClass('ui-state-disabled').prop('disabled', false).button("refresh");
            $("#target012-1 input.allchecker1").prop('checked', false).css('visibility', 'visible');
          };


        } else {
          return false;//フォームが空欄だった場合はなにもしない
        };

      });



      ////削除ボタンの動作 ===== =====
      $("#delbtn012-1").click(function() {

        $(this).blur();
        var listItemNum012 = $("#target012-main.allcheckTB").find("td input.allCheckItems").length;//項目の個数

        if ( listItemNum012 <= 0 ){

          //すでに0以下なら無効(削除ボタン)
          $("#delbtn012-1").addClass('ui-button-disabled').addClass('ui-state-disabled').prop('disabled', true);

        } else {

          //チェックボックスにチェックが入っているかを確認し、チェックが入ってるテーブル行については行(trタグと内包する要素)ごと削除する
          $('#target012-main.allcheckTB').find('td input.allCheckItems:checked').each(function() {
            $(this).parents("tr").remove();
          });

          //項目数削除に伴ってテーブル1行ごとの色を付けなおし
          $("#target012-main tr").removeClass("even-row");
          $("#target012-main tr:odd").addClass("even-row");

          var listItemNum012 = $("#target012-main.allcheckTB").find("td input.allCheckItems").length;//項目の個数　取得し再設定
          $("p.searchResultHitCount span.cnt012").text( listItemNum012 + "件/" );//件数表示書き換え

          $("#target012-1 input.allchecker1").prop('checked', false);


          //削除ボタンを押したあと
          if( listItemNum012 >= maxN012 ) {
            //最大値なら無効(追加ボタン)
            $("#addbtn012-1").addClass('ui-button-disabled').addClass('ui-state-disabled').removeClass('ui-state-hover').prop('disabled', true);
            //有効(削除ボタン)
            $("#delbtn012-1").removeClass('ui-button-disabled').removeClass('ui-state-disabled').prop('disabled', false).button("refresh");
            $("#target012-1 input.allchecker1").prop('checked', false).css('visibility', 'visible');
          } else if ( listItemNum012 <= 0 ) {
            //0以下であっても最大ではないので有効(追加ボタン)
            $("#addbtn012-1").removeClass('ui-button-disabled').removeClass('ui-state-disabled').prop('disabled', false).button("refresh");

            //0以下なら無効(削除ボタン)
            $("#delbtn012-1").addClass('ui-button-disabled').addClass('ui-state-disabled').removeClass('ui-state-hover').prop('disabled', true);
            $("#target012-1 input.allchecker1").prop('checked', false).css('visibility', 'hidden');
          } else {
            //最大ではなければ有効(追加ボタン)
            $("#addbtn012-1").removeClass('ui-button-disabled').removeClass('ui-state-disabled').prop('disabled', false).button("refresh");
            //0以下ではないので有効(削除ボタン)
            $("#delbtn012-1").removeClass('ui-button-disabled').removeClass('ui-state-disabled').prop('disabled', false).button("refresh");
            $("#target012-1 input.allchecker1").prop('checked', false).css('visibility', 'visible');
          };

        };

      });

    });







//  メールアドレス欄 編集可能化チェック  -016  only ===== ===== ===== ===== ===== =====
//
    $(function(){

      ////ページ読み込み完了時に
      ////メールアドレス1 のメールアドレス欄に既定の値が存在している場合は
      ////  入力欄を非アクティブにし非表示、別途既定の値をただのテキストとして表示
      ////  再入力欄は非アクティブにしたうえで設定済みの値をクリア
      ////そうではないの場合（空、未設定、初期？）は入力欄をアクティブ、変更ボタン削除
      if( ($('form input.emailEdit1a').val()) && ($('form input.emailEdit1b').val()) != "" ) {
        $(".emailEdit1a:text").prop('disabled', true);
        $(".emailEdit1b:text").prop('disabled', true).val("");

        var tgTxt1 = $(".emailEdit1a:text").val();

        $(".emailEdit1a").after("<span id='em1' class='noEditTxtField w320'></span>");
        $("#em1").append(tgTxt1);
        $(".emailEdit1a").css("display", "none");

      } else {
        $(".emailEdit1a:text").prop('disabled', false);
        $(".emailEdit1b:text").prop('disabled', false);
        $("button.btnemailchange1").remove();
      }

      ////ページ読み込み完了時に
      ////メールアドレス2 のメールアドレス欄に既定の値が存在している場合は
      ////  入力欄を非アクティブにし非表示、別途既定の値をただのテキストとして表示
      ////  再入力欄は非アクティブにしたうえで設定済みの値をクリア
      ////そうではないの場合（空、未設定、初期？）は入力欄をアクティブ、変更ボタン削除
      if( ($('form input.emailEdit2a').val()) && ($('form input.emailEdit2b').val()) != "" ) {
        $(".emailEdit2a:text").prop('disabled', true);
        $(".emailEdit2b:text").prop('disabled', true).val("");

        var tgTxt2 = $(".emailEdit2a:text").val();

        $(".emailEdit2a").after("<span id='em2' class='noEditTxtField w320'></span>");
        $("#em2").append(tgTxt2);
        $(".emailEdit2a").css("display", "none");

      } else {
        $(".emailEdit2a:text").prop('disabled', false);
        $(".emailEdit2b:text").prop('disabled', false);
        $("button.btnemailchange2").remove();
      }

      ////ページ読み込み完了時に
      ////メールアドレス3 のメールアドレス欄に既定の値が存在している場合は
      ////  入力欄を非アクティブにし非表示、別途既定の値をただのテキストとして表示
      ////  再入力欄は非アクティブにしたうえで設定済みの値をクリア
      ////そうではないの場合（空、未設定、初期？）は入力欄をアクティブ、変更ボタン削除
      if( ($('form input.emailEdit3a').val()) && ($('form input.emailEdit3b').val()) != "" ) {
        $(".emailEdit3a:text").prop('disabled', true);
        $(".emailEdit3b:text").prop('disabled', true).val("");

        var tgTxt3 = $(".emailEdit3a:text").val();

        $(".emailEdit3a").after("<span id='em3' class='noEditTxtField w320'></span>");
        $("#em3").append(tgTxt3);
        $(".emailEdit3a").css("display", "none");

      } else {
        $(".emailEdit3a:text").prop('disabled', false);
        $(".emailEdit3b:text").prop('disabled', false);
        $("button.btnemailchange3").remove();
      }

    });



    ////メールアドレス1 欄用の変更ボタン、クリックで入力欄アクティブ＆ボタン非表示
    $("button.btnemailchange1").click(function() {

      $(".emailEdit1a").css("display", "inline");
      $(".emailEdit1a:text").prop('disabled', false);
      $(".emailEdit1b:text").prop('disabled', false).val("");//再入力欄の値はクリア


      $("#em1").remove();
      $(this).css("visibility", "hidden").css("height", "0");
      ////$(this).remove();
      $(".emailEdit1a").focus().select();

    });

    ////メールアドレス2 欄用の変更ボタン、クリックで入力欄アクティブ＆ボタン非表示
    $("button.btnemailchange2").click(function() {

      $(".emailEdit2a").css("display", "inline");
      $(".emailEdit2a:text").prop('disabled', false);
      $(".emailEdit2b:text").prop('disabled', false).val("");//再入力欄の値はクリア


      $("#em2").remove();
      $(this).css("visibility", "hidden").css("height", "0");
      ////$(this).remove();
      $(".emailEdit2a").focus().select();

    });

    ////メールアドレス3 欄用の変更ボタン、クリックで入力欄アクティブ＆ボタン非表示
    $("button.btnemailchange3").click(function() {

      $(".emailEdit3a").css("display", "inline");
      $(".emailEdit3a:text").prop('disabled', false);
      $(".emailEdit3b:text").prop('disabled', false).val("");//再入力欄の値はクリア


      $("#em3").remove();
      $(this).css("visibility", "hidden").css("height", "0");
      ////$(this).remove();
      $(".emailEdit3a").focus().select();

    });










//  チェック確認のダイアログとダイアログ（jQueryUI使用タイプ）呼び出し設定 -012 only ===== ===== =====
//

    //項目チェックダイアログ -012 only
    $('.checkedDialog').dialog({

      autoOpen: false,
      draggable: false,
      resizable: false,
      title: $('#dialog_title').val(),
      closeOnEscape: false,
      modal: true,
      buttons: {
        'OK': function(){
          $(this).dialog('close');
        }
      }

   });

    //項目チェックダイアログ呼び出し -012 only
    $('.fn-call-checkedDialog').click(function(){

      $('.fn-call-checkedDialog').blur();

      if($('input.allCheckItems:checked').length == 0) {//未選択の場合
        //return false;
      } else {//1つ以上選択されている場合
        $('.checkedDialog').dialog('open');//ダイアログ呼び出し

        var chChkMsg = $('.checkedDialog p.checkChkMsg').text();
            chChkMsg = "選択された項目があります";//メッセージのテキストを変更する場合はコメントアウトを解除しメッセージ内容を設定
        $('.checkedDialog p.checkChkMsg').text(chChkMsg);

        return false;
      }

    });



//  削除確認のダイアログとダイアログ（jQueryUI使用タイプ）呼び出し設定 -018 only ===== ===== =====
//
    //削除確認ダイアログ -018 only
    arrButtons = {};
    arrButtons[Yes] =  function(){ $(this).dialog('close'); $('.deleteDialogAfter').dialog('open');};
    arrButtons[No] =  function(){ $(this).dialog('close');};
    $('.deleteDialog').dialog({
          autoOpen: false,
          draggable: false,
          resizable: false,
          title: $('#dialog_title').val(),
          closeOnEscape: false,
          modal: true,
          buttons: arrButtons
  });

//	    $('.deleteDialog').dialog({
//	      autoOpen: false,
//	      draggable: false,
//	      resizable: false,
//	      title: $('#dialog_title').val(),
//	      closeOnEscape: false,
//	      modal: true,
//	      buttons: {
//	        'はい': function(){
//	        ////削除時の動作をここのあたりに↓
//	        ////$('form').submit();
//	          $(this).dialog('close');
//	          $('.deleteDialogAfter').dialog('open');
//	//          location.href = "IV-018.html";
//	        },
//	        'いいえ': function(){
//	          $(this).dialog('close');
//	        }
//	      }
//    });

    //削除した後の確認ダイアログ -018 only
    $('.deleteDialogAfter').dialog({

      autoOpen: false,
      draggable: false,
      resizable: false,
      title: $('#dialog_title').val(),
      closeOnEscape: false,
      modal: true,
      buttons: {
        'OK': function(){
        ////削除時の動作をここのあたりに↓
        ////$('form').submit();
          $(this).dialog('close');
          location.href = "IV-018.html";
        }

      }

    });



    //項目未選択ダイアログ -018 only　（未使用）
    $('.deleteDialog2').dialog({

      autoOpen: false,
      draggable: false,
      resizable: false,
      title: $('#dialog_title').val(),
      closeOnEscape: false,
      modal: true,
      buttons: {
        'OK': function(){
          $(this).dialog('close');
        }
      }

   });


    //削除確認ダイアログ呼び出し -018 only
    $('.fn-call-deleteDialog').click(function(){

      $('.fn-call-deleteDialog').blur();

      if($('input.allCheckItems:checked').length == 0) {//未選択の場合
//        $('.deleteDialog2').dialog('open');//項目未選択ダイアログ呼び出し(未使用)
        $('.deleteDialog').dialog('open');//未選択であっても削除確認ダイアログ呼び出し
        return false;
      } else {//1つ以上選択されている場合
        $('.deleteDialog').dialog('open');//削除確認ダイアログ呼び出し
        return false;
      }

    });

//    //削除確認のダイアログ（confirm関数使用タイプ）  -018 only　（未使用）
//    $('.fn-call-deleteDialog').click(function(){
//
//      $('.fn-call-deleteDialog').blur();
//
//      if($('input.allCheckItems:checked').length == 0) {
//        alert('メッセージが選択されていません。');
//      } else {
//        return confirm('選択したメッセージを削除しますか？');
//      }
//
//    });





//  scroll table headertable and maintable width fixed -005 -012 -018 -023 ===== ===== =====
//
    $(function(){

      var tablew = $('table.scrollTableHead').width();
      $('table.scrollTableHead, table.scrollTableFoot').css('width', $('div.scrollTableIn').width()+2 + 'px');

      var w = $('div.scrollTableIn').width() - tablew;
      $('td.temp').css('width', $('div.scrollTableIn').width() - tablew).css('padding', 0).css('border-left', 0);

    });


//  scroll table in maintable height check and addClass -005 -012 -018 -023
//
    $(function(){

      //高さをpxをもとに設定（scrollするタイプのテーブルにclass追加class追加）
      var checkTH = $('table.clickable-rows').height();
      $('table.clickable-rows').addClass('borderBottomAdd');

      ////以下は未使用
      //if( checkTH < 132){
      //  $('table.clickable-rows').addClass('borderBottomAdd');
      //}

//      //高さを項目数をもとに設定（6項目より少ない場合にclass追加）
//      var checkTR = $('table.clickable-rows').find('tr').length;
//      if( checkTR < 6){
//        $('table.clickable-rows').addClass('borderBottomAdd');
//      }

    });



//  common functions ===== ===== ===== =====

//  Enterキー入力による送信を防止する（テキスト入力フィールド内およびチェックボックスフォーカス時）
//
    $(function(){

      $("input[type='text'],input[type='password'],input[type='radio'],input[type='checkbox'], div.scrollTableIn td").keypress(function(ev) {
        if ((ev.which && ev.which === 13) || (ev.keyCode && ev.keyCode === 13)) {
          return false;
        } else {
          return true;
        }
      });

    });


//  ログアウト確認ダイアログ all ===== ===== ===== =====
    arrButtons = {};
    arrButtons[Yes] =  function(){ $(this).dialog('close'); location.href = "Logout";};
    arrButtons[No] =  function(){ $(this).dialog('close');};
    $('.logoutDialog').dialog({
          autoOpen: false,
          draggable: false,
          resizable: false,
          title: $('#dialog_title').val(),
          closeOnEscape: false,
          modal: true,
          buttons: arrButtons
        });
    //ログアウト確認ダイアログ
//	    $('.logoutDialog').dialog({
//	      autoOpen: false,
//	      draggable: false,
//	      resizable: false,
//	      title: $('#dialog_title').val(),
//	      closeOnEscape: false,
//	      modal: true,
//	      buttons: {
//	        'はい': function(){
//	        ////ログアウト時の動作をここのあたりに↓
//	        ////$('form').submit();
//	          $(this).dialog('close');
//	          location.href = "Logout";
//	        },
//	        'いいえ': function(){
//	          $(this).dialog('close');
//	        }
//	      }
//	    });


    //ログアウト確認ダイアログ呼び出し
    $('.fn-call-logoutDialog').click(function(){

      $('.fn-call-logoutDialog').blur();
      $('.logoutDialog').dialog('open');//ログアウト確認ダイアログ呼び出し

      var logoutMsg = $('.logoutDialog p.logoutChkMsg').text();
      $('.logoutDialog p.logoutChkMsg').text(logoutMsg);

      return false;

    });



//  validationEngine（未使用）
//  $("form").validationEngine();



//  UI effects ===== ===== ===== =====

//  jQuery UI buttons -and- styled-table,styled-table3 addClass
//
    $(function(){

      $('.jquery-ui-buttons').find('input', 'a').button();
      $('.text-button').button();

      $('form table.styled-table tr:even, form table.styled-table3 tr:even').addClass('even-row');

    });


    $('.tooltip_icon').hover(
        // Hover over code
        function(){
            var title = $(this).attr('tip');
            $(this).data('tipText', title).removeAttr('tip');
            // Start 1.x ST-001
            // Start step 2.5 #1948
            if ($('.tooltip').html().toString() != title) {
                $('.tooltip').html(title);
            }
            $('.tooltip').fadeIn('slow');
            // End step 2.5 #1948
            // End 1.x ST-001
        },
        // Hover out code
        function() {
            $(this).attr('tip', $(this).data('tipText'));
            // Start step 2.5 #1948
            $('.tooltip').hide();
            $('.tooltip').css({ top: 0, left: 0 });
            // End step 2.5 #1948
        }).mousemove(function(e) {
            // Start step 2.5 #1948
            var title = $(this).attr('tip');
            if ($('.tooltip').html().toString() != title) {
                $('.tooltip').html(title);
            }
            
            var mousex = e.pageX - 50;
            var mousey = e.pageY - (20 + $('.tooltip').outerHeight());
            
            if ($(this).offset().left + 100 > $('.icWrapper').offset().left + $('.icWrapper').outerWidth()) {
                mousex = e.pageX - $('.tooltip').outerWidth() + 50;
            } 
            
            if ($(window).height() - ($(this).offset().top - $(window).scrollTop()) > $('.tooltip').outerHeight() + 20) {
                mousey = e.pageY + 5;
            } 
            // End step 2.5 #1948
            $('.tooltip').css({ top: mousey, left: mousex });
        });
    
      $("body").on('mouseenter', 'table.hoverable-row tr',function(){
            $(this).addClass('hover_tr');
          }).on('mouseleave', 'table.hoverable-row tr',function(){
            $(this).removeClass('hover_tr');
          });
      $('table.hoverable-row').addClass('borderBottomAdd');
});
//(C) NTT Communications  2013  All Rights Reserved