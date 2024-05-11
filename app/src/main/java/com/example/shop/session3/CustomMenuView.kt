package com.example.shop.session3

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.shop.R

class CustomMenuView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Путь, по которому будет рисоваться фигура
    private val path = Path()

    // Кисть для рисования фигуры
    private val paint = Paint().apply {
        isAntiAlias = true // Сглаживание краев
        style = Paint.Style.FILL // Стиль заливки
        color = ContextCompat.getColor(context, R.color.white) // Цвет фигуры
    }

    // Габариты и позиции фигуры
    private var navigationWidth: Float = 0f
    private var navigationHeight: Float = 0f
    private var curveRadius: Float = 0f
    private var fabRadius: Float = 0f
    private var fabMargin: Float = 20f  // Расстояние от FAB до верхней части этого представления

    init {
        // Инициализация FAB и других элементов, если это необходимо
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        createCurvedPath() // Создаем кривой путь

        canvas.drawPath(path, paint) // Рисуем фигуру на канве
    }

    private fun createCurvedPath() {
        path.reset() // Сбрасываем путь

        // Определяем свойства для кривой нижней навигации
        val fabCenter = navigationWidth / 2
        val curveStartVerticalOffset = navigationHeight - curveRadius * 2
        val curveEnd = navigationHeight

        // Перемещаемся в начальную точку
        path.moveTo(0f, curveStartVerticalOffset)

        // Рисуем линию к началу кривой слева
        path.lineTo(fabCenter - fabRadius - fabMargin, curveStartVerticalOffset)

        // Рисуем дугу для левой кривой
        path.arcTo(
            fabCenter - fabRadius - fabMargin,
            curveStartVerticalOffset,
            fabCenter + fabRadius + fabMargin,
            curveEnd,
            180f,
            -180f,
            false
        )

        // Рисуем линию до конца кривой справа
        path.lineTo(navigationWidth, curveStartVerticalOffset)

        // Рисуем последнюю линию вниз до нижней части представления
        path.lineTo(navigationWidth, curveEnd)

        // Закрываем путь обратно в начальную точку
        path.lineTo(0f, curveEnd)
        path.close()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        navigationWidth = w.toFloat() // Ширина представления
        navigationHeight = h.toFloat() // Высота представления
        curveRadius = navigationHeight / 2 // Радиус кривой
        fabRadius = navigationHeight * 0.4f  // Размер FAB (плавающей кнопки)
    }
}
