package com.pedro.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Grafico a barre settimanale che mostra l'utilizzo dei social per gli ultimi 7 giorni.
 * Stile brutale: barre bianche/grigie su sfondo nero, etichette minimal.
 */
public class WeeklyChartView extends View {

    private long[] dailyMs = new long[7];
    private String[] labels = {"", "", "", "", "", "", ""};

    private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint todayBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint emptyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF barRect = new RectF();

    public WeeklyChartView(Context context) { super(context); init(); }
    public WeeklyChartView(Context context, AttributeSet attrs) { super(context, attrs); init(); }
    public WeeklyChartView(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); init(); }

    private void init() {
        barPaint.setColor(0xFF888888);
        barPaint.setStyle(Paint.Style.FILL);

        todayBarPaint.setColor(0xFFFFFFFF);
        todayBarPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextAlign(Paint.Align.CENTER);

        labelPaint.setColor(0xFF888888);
        labelPaint.setTextAlign(Paint.Align.CENTER);

        gridPaint.setColor(0x30FFFFFF);
        gridPaint.setStrokeWidth(1f);

        emptyPaint.setColor(0xFF555555);
        emptyPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(long[] millisPerDay, String[] dayLabels) {
        if (millisPerDay != null && millisPerDay.length == 7) {
            System.arraycopy(millisPerDay, 0, this.dailyMs, 0, 7);
        }
        if (dayLabels != null && dayLabels.length == 7) {
            System.arraycopy(dayLabels, 0, this.labels, 0, 7);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        float density = getResources().getDisplayMetrics().density;
        float topPadding = 28 * density;
        float bottomPadding = 24 * density;
        float sidePadding = 8 * density;

        float chartHeight = h - topPadding - bottomPadding;
        float chartWidth = w - sidePadding * 2;

        // Trova il massimo per scalare le barre
        long maxMs = 1;
        boolean hasData = false;
        for (long ms : dailyMs) {
            if (ms > maxMs) maxMs = ms;
            if (ms > 0) hasData = true;
        }

        float barSpacing = chartWidth / 7f;
        float barWidth = barSpacing * 0.55f;

        textPaint.setTextSize(9 * density);
        labelPaint.setTextSize(10 * density);

        float baseline = h - bottomPadding;

        // Linea di base
        canvas.drawLine(sidePadding, baseline, w - sidePadding, baseline, gridPaint);

        // Se non ci sono dati, mostra "—"
        if (!hasData) {
            emptyPaint.setTextSize(12 * density);
            canvas.drawText("—", w / 2f, h / 2f, emptyPaint);
        }

        for (int i = 0; i < 7; i++) {
            float cx = sidePadding + barSpacing * i + barSpacing / 2f;
            float barLeft = cx - barWidth / 2f;
            float barRight = cx + barWidth / 2f;

            // Altezza barra proporzionale al massimo
            float ratio = (float) dailyMs[i] / maxMs;
            float minBarH = 3 * density;
            float barH = Math.max(ratio * chartHeight, dailyMs[i] > 0 ? minBarH : 0);

            float barTop = baseline - barH;
            barRect.set(barLeft, barTop, barRight, baseline);

            // Oggi (indice 6) bianco solido, gli altri grigi
            canvas.drawRect(barRect, (i == 6) ? todayBarPaint : barPaint);

            // Valore sopra la barra
            if (dailyMs[i] > 0) {
                canvas.drawText(formatMs(dailyMs[i]), cx, barTop - 6 * density, textPaint);
            }

            // Etichetta giorno sotto
            canvas.drawText(labels[i], cx, h - 6 * density, labelPaint);
        }
    }

    private String formatMs(long ms) {
        long totalMin = ms / (1000 * 60);
        if (totalMin >= 60) {
            long hours = totalMin / 60;
            long mins = totalMin % 60;
            return hours + "h" + (mins > 0 ? mins : "");
        }
        return totalMin + "m";
    }
}
