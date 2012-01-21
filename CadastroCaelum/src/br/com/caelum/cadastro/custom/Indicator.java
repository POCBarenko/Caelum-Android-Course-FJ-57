package br.com.caelum.cadastro.custom;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Indicator extends View implements OnTouchListener {

	private static  int MAXIMO_LARGURA = 200;
	private static final int MAXIMO_ALTURA = 30;
	private float x;

	public Indicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
		setMinimumHeight((int) MAXIMO_ALTURA);
		setMinimumWidth((int) MAXIMO_LARGURA);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		this.x = event.getX();
		invalidate();
		return false;
	}

	public double getValor() {
		return x * 10 / MAXIMO_LARGURA;
	}

	public void setValor(double valor) {
		Log.i("Indicator", "ValorA: " + valor);
		this.x = (float) (MAXIMO_LARGURA * valor / 10);
		Log.i("Indicator", "Valor: " + x);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.i("Indicator", "Desenhando " + x);
		canvas.drawColor(Color.GRAY);

		Paint p = new Paint();
		p.setColor(Color.RED);

		RectF r = new RectF(0, 0, x, MAXIMO_ALTURA);
		canvas.drawRect(r, p);
		
		Paint paintBlack = new Paint();
		paintBlack.setColor(Color.BLACK);
		paintBlack.setTextSize(getHeight()-1);
		paintBlack.setTextAlign(Align.CENTER);
		canvas.drawText(new DecimalFormat("0.09").format(getValor()), MAXIMO_LARGURA/2, getHeight()-1, paintBlack);
	}

	public void setLargura(int widthPixels) {
		MAXIMO_LARGURA = widthPixels;
	}
}
