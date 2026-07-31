package com.example.zapretcalc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.time.chrono.ThaiBuddhistDate;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;
import java.io.IOException;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

  LinearLayout screenCalc;
  LinearLayout screenSettings;

  Random rand = new Random();
  List<Integer> BlackListNum = new ArrayList<>();

  boolean isRkn = false;
  boolean isExitFromRkn = false;
  int attempts = 0;
  boolean isAdmin = false;
  boolean isVpn = false;
  double chance = 0.20;
  double randomForRkn = rand.nextDouble();
  boolean offToasts = false;
  private static final String version = "🔥 1.9 test build";
  private static final String news = "✅ добавлена ссылка на GitHub (весь исходный код на нем)\n✅ исправлены баги (наверное)";
  int idPlayer = rand.nextInt(); // ни на что пока не влияет
  boolean isNormalCalc = false;
  boolean isRoot = false;
  boolean isSoonRootUser = false;
  boolean flag = false;
  int count = 0;

  String vpnOOO; // On or Off
  // TODO: доделать брат, обязательно
  String toastOOO; // On or Off

  private SharedPreferences prefs;
  private SharedPreferences.Editor editor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    screenCalc = findViewById(R.id.screenCalc);
    screenSettings = findViewById(R.id.screenSettings);

    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

    View decorView = getWindow().getDecorView();
    decorView.setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

    prefs = getSharedPreferences("CalcSaver", MODE_PRIVATE);
    editor = prefs.edit();
    loadData();

    EditText num1 = findViewById(R.id.input1);
    EditText num2 = findViewById(R.id.input2);
    Button btn = findViewById(R.id.btnSum);
    Button btnUmn = findViewById(R.id.btnUmn);
    TextView result = findViewById(R.id.result);
    Button btnDel = findViewById(R.id.btnDel);
    Button btnMin = findViewById(R.id.btnMin);
    Button btnRkn = findViewById(R.id.btnRkn);
    Button btnClear = findViewById(R.id.btnClear);
    Button btnOnlyAdmin = findViewById(R.id.btnOnlyAdmin);
    Button btnInfo = findViewById(R.id.btnInfo);
    btnOnlyAdmin.setVisibility(View.INVISIBLE);
    Button btnReq = findViewById(R.id.btnReq);
    Button btnNext = findViewById(R.id.btnNextScreenSettings);
    Button btnExitFromSettings = findViewById(R.id.btnExitFromSettings);
    EditText cmd = findViewById(R.id.cmd);
    Button btnCmd = findViewById(R.id.btnCmd);
    EditText cmdTwo = findViewById(R.id.cmdTwo);

    btnNext.setOnClickListener(v -> {
      screenCalc.setVisibility(View.GONE);
      screenSettings.setVisibility(View.VISIBLE);
    });

    btnExitFromSettings.setOnClickListener(v -> {
      screenSettings.setVisibility(View.GONE);
      screenCalc.setVisibility(View.VISIBLE);
    });

    btnCmd.setOnClickListener(v -> {
      String a = cmd.getText().toString();
      String b = cmdTwo.getText().toString();

      if (a == null && a.isEmpty()) {
        return;
      }

      if (b == null && b.isEmpty()) {
        return;
      }

      if (a.equals("/addServer")) {
        // ТУТ НАЧИНАЕТСЯ
        helloToast("запрос на сервер (GET) с url: " + b, 1);
        String url = b;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
          @Override
          public void onFailure(Call call, IOException e) {
            runOnUiThread(() -> helloToast("Ошибка соединения: " + e.getMessage(), 1));
          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {
            helloToast("подождите...", 0);
            if (response.isSuccessful()) {
              String responseBody = response.body().string();
              runOnUiThread(() -> {
                helloToast("ответ сервера: " + responseBody, 1);
              });
            } else {
              runOnUiThread(() -> helloToast("Ошибка сервера: " + response.code(), 1));
            }
          }
        });
        // ТУТ КОНЧАЕТСЯ
      } else if (a.equals("/admin") && b.equals("check")) {
        if (isAdmin) {
          helloToast("yep", 0);
        } else if (!isAdmin) {
          helloToast("nope", 0);
        } else {
          helloToast("error", 1);
        }
      } else if (a.equals("/admin") && b.equals("get_t")) {
        isAdmin = true;
        helloToast("Admin on", 1);
      } else if (a.equals("/admin") && b.equals("get_f")) {
        isAdmin = false;
        helloToast("Admin off", 1);
      } else if (a.equals("/id") && b.equals("check")) {
        helloToast("your id: " + idPlayer, 0);
      } else if (a.equals("/calc") && b.equals("normal")) {
        isNormalCalc = true;
        helloToast("Normal Calc Active", 0);
      } else if (a.equals("/calc") && b.equals("just")) {
        isNormalCalc = false;
        helloToast("Normal Calc Inactive", 0);
      } else if (a.equals("/say") && !b.isEmpty()) {
        helloToast(b, 0);
      } else if (a.equals("/fsb") && b.equals("check")) {
        helloToast("на самом деле я собираюсь добавить ФСБ помимо РКН.Скоро...", 0);
      } else if (a.equals("/help")) {
        String help = "1 поле: /admin 2 поле: check — проверка на админа"
            + "1 поле: /fsb 2 поле: check — проверка на ФСБ"
            + "1 поле: /calc 2 поле: normal — включить нормальный вычислительный калькулятор"
            + "1 поле: /calc 2 поле: just — выключить нормальный калькулятор"
            + "1 поле: /say 2 поле: любое слово — сказать любое слово (тост)";

        if (isRoot) {
          help += "/addServer (GET req)" + "/admin : get_t" + "/admin : get_f" + "/id : check";
        }

        helloToast(help, 1);

      } else if (isAdmin && a.equals("/root") && b.equals("/rooted")) {
        isSoonRootUser = true;
        helloToast("ты знаешь куда жать", 1);
      } else {
        helloToast("такой команды не существует", 1);
      }

    });

    new AlertDialog.Builder(this).setTitle("Что нового").setMessage(version + "\n" + news)
        .setPositiveButton("Ясно", (d, w) ->

        {
        }).setNegativeButton("Ясно.Отключить тосты (test)", (d, w) -> {
          helloToast("последний тост (тосты будут отключены)", 0);
          offToasts = true;
        }).show();

    if (isAdmin) {
      btnOnlyAdmin.setVisibility(View.VISIBLE);
    }

    if (vpnOOO == null || vpnOOO.isEmpty()) {
      vpnOOO = "❌";
    }

    btn.setOnClickListener(v -> {
      int a = Integer.parseInt(num1.getText().toString());
      int b = Integer.parseInt(num2.getText().toString());

      if (isRkn && BlackListNum.contains(a)) {
        result.setText("Blocked!");
        helloToast("blocked!", 0);
      }

      if (isRkn && a == 1488 || b == 1488) {
        if (isVpn) {
          result.setText("ВПН блокировал попытку запрета.Равно: " + (a + b));
          helloToast("ВПН защитил тебя", 0);
          helloToast("равно: " + rand.nextInt(), 0);
        } else {
          result.setText("1488 под запретом!");
          helloToast("запрещено!", 0);
        }
      } else if (isRkn && rand.nextDouble() < chance) {
        if (isRkn && rand.nextDouble() < chance) {
          if (isVpn) {
            result.setText("ВПН блокировал попытку запрета.Равно: " + rand.nextInt());
            helloToast("ВПН защитил тебя", 0);
            helloToast("равно: " + rand.nextInt(), 0);
          } else {
            if (!BlackListNum.contains(a)) {
              BlackListNum.add(a);
              result.setText("this value blocked!");
            } else {
              result.setText("this value blocked!");
            }
          }
        }
      } else if (isNormalCalc) {
        result.setText("Результат: " + (a + b));
        helloToast("Результат: " + (a + b), 0);
      } else {
        result.setText("Результат: " + rand.nextInt());
        helloToast("Результат: " + rand.nextInt(), 0);
      }
    });

    btnUmn.setOnClickListener(v -> {
      int a = Integer.parseInt(num1.getText().toString());
      int b = Integer.parseInt(num2.getText().toString());

      if (isRkn && a == 10102090 && b == 04) {
        isExitFromRkn = true;
        result.setText("чтобы отключить режим РКН\nТебе нужно нажать на вычитание");
      } else if (isNormalCalc) {
        result.setText("Результат: " + (a * b));
        helloToast("Результат: " + (a * b), 0);
      } else {
        result.setText("Результат: " + rand.nextInt());
      }
    });

    btnDel.setOnClickListener(v -> {
      int a = Integer.parseInt(num1.getText().toString());
      int b = Integer.parseInt(num2.getText().toString());

      if (isNormalCalc) {
        if (b == 0) {
          result.setText("dont use http");
        } else {
          result.setText("Результат: " + (a / b));
          helloToast("Результат: " + (a / b), 0);
        }
      } else {
        result.setText("Результат: " + rand.nextInt());
        helloToast("Результат: " + rand.nextInt(), 0);
      }

      if (isAdmin && a == 962580 && b == 5050) {
        new AlertDialog.Builder(this).setTitle("Меню разработчика").setMessage(
            "добро пожаловать в меню разработчика,оно не предназначено для обычных пользователей.Внимание: вы можете случайно крашнуть свое приложение,ещё раз прошу вас выйти с этого меню если зашли")
            .setPositiveButton("send req on server (local http)", (d, w) -> {
              dialog();
            }).setNegativeButton("send req on server (test http)", (d, w) -> {
              sendRequestToServer("https://api.github.com/zen");
            }).show();
      } else {
        return;
      }
    });

    btnMin.setOnClickListener(v -> {
      int a = Integer.parseInt(num1.getText().toString());
      int b = Integer.parseInt(num2.getText().toString());

      if (isExitFromRkn && isRkn) {
        isRkn = false;
        isExitFromRkn = false;
        result.setText("хм..Ладно,код правильный.Режим отключен");
        helloToast("Код правильный,доступ открыт", 0);
      } else if (isNormalCalc) {
        result.setText("Результат: " + (a - b));
        helloToast("Результат: " + (a - b), 0);
      } else {
        result.setText("Сумма: " + rand.nextInt());
        helloToast("Сумма: " + rand.nextInt(), 0);
      }
    });

    btnRkn.setOnClickListener(v -> {
      isRkn = true;
      result.setText("RKN MODE ON");
      helloToast("RKN MODE ON", 1);
    });

    btnClear.setOnClickListener(v -> {
      if (isRkn) {
        attempts++;

        if (isAdmin) {

          helloToast("вы уже являетесь админом", 0);
          return;
        }

        if (attempts > 3) {
          attempts = 0;
          isAdmin = true;
          btnOnlyAdmin.setVisibility(View.VISIBLE);
          result.setText("admin king on!");
          helloToast("admin king on!", 1);
        }
      } else {
        num1.setText("");
        num2.setText("");
        result.setText("cleared!");
        helloToast("cleared", 1);
      }
    });

    btnOnlyAdmin.setOnClickListener(v -> {
      if (!isAdmin) {
        result.setText("эта кнопка запрещена для не админов!");
        helloToast("запрещено!", 0);
      } else {
        new AlertDialog.Builder(this).setTitle("Админ меню").setMessage("Выберите один из пунктов")
            .setPositiveButton("Отменить РКН", (d, w) -> {
              isRkn = false;
              helloToast("rkn mode off", 0);
            }).setNegativeButton("ВПН " + (vpnOOO), (d, w) -> {
              if (!isVpn) {
                isVpn = true;
                vpnOOO = "✅";
                result.setText("vpn on");
                helloToast("vpn on", 1);
              } else if (isVpn) {
                isVpn = false;
                vpnOOO = "❌";
                result.setText("vpn off");
                helloToast("vpn off", 1);
              } else {
                return;
              }
            }).setNeutralButton("Добавить шансы", (d, w) -> {
              chance += 0.1;
              chance = Math.clamp(chance, 0.0, 1.0);
              result.setText("шансы изменены на " + chance);
              helloToast("шансы изменены на " + chance, 0);
            }).show();
      }
    });

    btnInfo.setOnClickListener(v -> {
      new AlertDialog.Builder(this).setTitle("Информация о игре").setMessage(
          "Запретный калькулятор!\nВнимание всем,этот калькулятор является экстремистским,поэтому он должен быть заблокирован\nВерсия экстремистского приложения: "
              + version + "\nСоздатель: hot pupsik (ник главного уебка)")
          .setPositiveButton("Ясно", (d, w) -> {
          })
          .setNegativeButton("Жми!", (d, w) -> {
            if (isSoonRootUser) {
              if (count <= 5) {
                count++;
                helloToast("тап тап: " + count, 1);
              } else if (count == 5) {
                isRoot = true;
                helloToast("ты имеешь рут права", 1);
              } else {
                helloToast("???", 1);
              }
              
              if (count > 5) {
                helloToast("ты уже имеешь рут права", 1);
                return;
              }
              
            } else {
              helloToast("что ты здесь ожидал увидеть?", 1);
            }
          }).show();
    });

    btnReq.setOnClickListener(v -> {
      String url = "http://127.0.0.1:5000/news";

      OkHttpClient client = new OkHttpClient();

      Request request = new Request.Builder().url(url).build();

      client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
          runOnUiThread(() -> helloToast("Ошибка соединения: " + e.getMessage(), 1));
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
          helloToast("подождите...", 0);
          if (response.isSuccessful()) {
            String responseBody = response.body().string();
            runOnUiThread(() -> {
              new AlertDialog.Builder(MainActivity.this).setTitle("Ответ сервера")
                  .setMessage(responseBody).setPositiveButton("Ясно", (d, w) -> {
                  }).show();
            });
          } else {
            runOnUiThread(() -> helloToast("Ошибка сервера: " + response.code(), 1));
          }
        }
      });
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    saveData();
  }

  @Override
  protected void onPause() {
    super.onPause();
    saveData();
  }

  public void helloToast(String text, int value) {

    if (offToasts) {
      return;
    }

    if (text == null || text.isEmpty()) {
      return;
    }

    if (value == 0) {
      Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    } else if (value == 1) {
      Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    } else {
      return;
    }
  }

  public void dialog() {
    new AlertDialog.Builder(this).setTitle("выберите с какого IP будет идти запрос")
        .setMessage("???").setPositiveButton("127.0.0.1:5000", (d, w) -> {
          sendRequestToServer("http://127.0.0.1:5000");
        }).setNegativeButton("192.168.0.11:5000", (d, w) -> {
          sendRequestToServer("http://192.168.0.11:5000");
        }).setNeutralButton("localhost:5000", (d, w) -> {
          sendRequestToServer("http://localhost:5000");
        }).show();
  }

  private void saveData() {
    editor.putBoolean("isRkn", isRkn);
    editor.putBoolean("isAdmin", isAdmin);
    editor.putBoolean("isVpn", isVpn);
    editor.putInt("attempts", attempts);
    editor.putBoolean("isExitFromRkn", isExitFromRkn);
    editor.putString("vpnOOO", vpnOOO);
    editor.putInt("idPlayer", idPlayer);
    editor.apply();
  }

  private void loadData() {
    isRkn = prefs.getBoolean("isRkn", false);
    isAdmin = prefs.getBoolean("isAdmin", false);
    isVpn = prefs.getBoolean("isVpn", false);
    attempts = prefs.getInt("attempts", 0);
    isExitFromRkn = prefs.getBoolean("isExitFromRkn", false);
    vpnOOO = prefs.getString("vpnOOO", vpnOOO);
    idPlayer = prefs.getInt("idPlayer", 0);

    if (isRkn) {
      helloToast("от РКН никуда не сбежишь", 0);
    }
  }

  private void sendRequestToServer(String link) {
    String url = link;

    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder().url(url).build();

    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        runOnUiThread(() -> helloToast("Ошибка соединения: " + e.getMessage(), 1));
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
          String responseBody = response.body().string();
          runOnUiThread(() -> helloToast("Ответ сервера: " + responseBody, 1));
        } else {
          runOnUiThread(() -> helloToast("Ошибка сервера: " + response.code(), 1));
        }
      }
    });
  }

}
