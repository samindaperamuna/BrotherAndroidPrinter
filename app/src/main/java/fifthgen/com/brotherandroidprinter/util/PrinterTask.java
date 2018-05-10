package fifthgen.com.brotherandroidprinter.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterStatus;

public class PrinterTask extends AsyncTask<String, Void, String> {

    private String ip;
    private int labelNameIndex;

    private Printer printer;
    private String message;


    public PrinterTask(String ip, int labelNameIndex) {
        this.ip = ip;
        this.labelNameIndex = labelNameIndex;
    }

    @Override
    protected String doInBackground(String... params) {

        if (params.length > 0) {
            if (setupPrinter(ip, labelNameIndex)) {
                Bitmap imageToPrint = ImageUtil.textAsBitMap(params);

                print(imageToPrint);
            }
        }

        return message;
    }

    private boolean setupPrinter(String ip, int labelNameIndex) {

        try {
            printer = new Printer();

            PrinterInfo printerInfo = printer.getPrinterInfo();

            printerInfo.printerModel = PrinterInfo.Model.QL_820NWB;
            printerInfo.port = PrinterInfo.Port.NET;
            printerInfo.paperSize = PrinterInfo.PaperSize.CUSTOM;
            printerInfo.orientation = PrinterInfo.Orientation.LANDSCAPE;
            printerInfo.valign = PrinterInfo.VAlign.MIDDLE;
            printerInfo.align = PrinterInfo.Align.CENTER;
            printerInfo.printMode = PrinterInfo.PrintMode.ORIGINAL;
            printerInfo.numberOfCopies = 1;
            printerInfo.ipAddress = ip;

            printerInfo.labelNameIndex = labelNameIndex;

            printerInfo.isAutoCut = true;
            printerInfo.isCutAtEnd = false;
            printerInfo.isHalfCut = false;
            printerInfo.isSpecialTape = false;

            printer.setPrinterInfo(printerInfo);

            return true;
        } catch (Exception e) {
            Log.e(getClass().getName(), "Couldn't initialize the printer.");
        }

        return false;
    }

    private void print(final Bitmap image) {
        PrinterStatus printerStatus;
        printer.startCommunication();
        printerStatus = printer.printImage(image);

        printer.endCommunication();

        if (printerStatus.errorCode != PrinterInfo.ErrorCode.ERROR_NONE) {
            message = "Couldn't print label. Error code : " + printerStatus.errorCode;
        }

        message = "All documents successfully sent to printer.";
    }
}