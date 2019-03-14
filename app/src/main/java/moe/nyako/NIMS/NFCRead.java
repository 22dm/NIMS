package moe.nyako.NIMS;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.support.v7.app.AppCompatActivity;

public class NFCRead extends AppCompatActivity {

    public NfcAdapter nfcAdapter;
    public PendingIntent pendingIntent;
    public boolean nfc_enabled = false;

    //初始化 NFC 硬件
    public void nfcInit() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        nfc_enabled = nfcAdapter != null && nfcAdapter.isEnabled();

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //读取到的标签
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (tag != null)
            processTag(tag);
    }

    public void processTag(Tag tag) {
        MifareClassic mifareClassic = MifareClassic.get(tag);
        StringBuilder out = new StringBuilder();

        try {
            mifareClassic.connect();

            //校园卡 0 扇区 KeyA 为 010203040506
            byte[] key = {1, 2, 3, 4, 5, 6};
            mifareClassic.authenticateSectorWithKeyA(0, key);

            //学号位于 0 扇区 1 块
            byte[] data = mifareClassic.readBlock(1);
            for (byte b : data) out.append(Integer.toHexString(b & 0xf));
        } catch (Exception ignored) {
        }

        //返回学号
        output(out.substring(0, 9));
    }

    //读取完学号后会调用此函数
    protected void output(String out) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }
}
