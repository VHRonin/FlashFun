package com.example.flashfun.data

object Repository {

    val cardSets: List<FlashCardSet> = listOf(
        FlashCardSet(
            id = "kotlin_basics",
            title = "Kotlin Basics",
            description = "Основы языка Kotlin",
            emoji = "🎯",
            cards = listOf(
                FlashCard(
                    question = "Что такое val в Kotlin?",
                    answer = "val — неизменяемая ссылка (аналог final в Java). После инициализации значение нельзя переприсвоить."
                ),
                FlashCard(
                    question = "Чем отличается var от val?",
                    answer = "var — изменяемая переменная, val — неизменяемая. Рекомендуется всегда использовать val, если переприсвоение не нужно."
                ),
                FlashCard(
                    question = "Что такое data class?",
                    answer = "data class автоматически генерирует equals(), hashCode(), toString(), copy() и componentN() функции на основе параметров конструктора."
                ),
                FlashCard(
                    question = "Что такое object declaration?",
                    answer = "object — создаёт singleton. Экземпляр создаётся при первом обращении и существует в единственном числе на протяжении жизни приложения."
                ),
                FlashCard(
                    question = "Что такое extension function?",
                    answer = "Функция-расширение позволяет добавлять методы к существующим классам без их изменения и без наследования."
                ),
                FlashCard(
                    question = "Что такое sealed class?",
                    answer = "sealed class — закрытая иерархия классов. Все подклассы должны быть объявлены в том же файле. Удобен для when-выражений с гарантированным exhaustive."
                ),
                FlashCard(
                    question = "Что такое companion object?",
                    answer = "companion object — объект внутри класса, доступный через имя класса. Аналог статических членов Java, но с доступом к приватным членам класса."
                ),
            )
        ),
        FlashCardSet(
            id = "compose_basics",
            title = "Jetpack Compose",
            description = "UI-фреймворк для Android",
            emoji = "🖌️",
            cards = listOf(
                FlashCard(
                    question = "Что такое Composable-функция?",
                    answer = "Функция, помеченная аннотацией @Composable, описывает часть UI декларативно. Compose отслеживает состояние и перекомпоновывает UI при его изменении."
                ),
                FlashCard(
                    question = "Что такое State в Compose?",
                    answer = "State<T> — обёртка над значением, изменение которой вызывает рекомпозицию всех Composable, читающих это состояние."
                ),
                FlashCard(
                    question = "Чем remember отличается от rememberSaveable?",
                    answer = "remember сохраняет значение между рекомпозициями, но теряет его при пересоздании. rememberSaveable дополнительно переживает смену конфигурации."
                ),
                FlashCard(
                    question = "Что такое Modifier в Compose?",
                    answer = "Modifier — цепочка трансформаций, применяемых к Composable: размер, отступы, фон, кликабельность и др. Порядок применения важен."
                ),
                FlashCard(
                    question = "Что такое hoisting состояния?",
                    answer = "Паттерн поднятия состояния выше по дереву Composable: Composable принимает value и onValueChange, а родитель управляет состоянием. Упрощает тестирование и повторное использование."
                ),
                FlashCard(
                    question = "Что такое LaunchedEffect?",
                    answer = "LaunchedEffect запускает корутину в scope Composable. Перезапускается при изменении key. Используется для side-эффектов: запросов, анимаций, навигации."
                ),
            )
        ),
        FlashCardSet(
            id = "android_arch",
            title = "Android Architecture",
            description = "Архитектурные паттерны",
            emoji = "🏗️",
            cards = listOf(
                FlashCard(
                    question = "Что такое ViewModel?",
                    answer = "ViewModel хранит UI-состояние и переживает смену конфигурации (поворот экрана). Не хранит ссылки на View/Context."
                ),
                FlashCard(
                    question = "Что такое StateFlow?",
                    answer = "StateFlow — горячий поток с текущим значением (value). Всегда имеет начальное состояние. В Compose собирается через collectAsStateWithLifecycle()."
                ),
                FlashCard(
                    question = "Что такое LiveData?",
                    answer = "LiveData — lifecycle-aware observable. В Compose устарел в пользу StateFlow, но поддерживается через observeAsState()."
                ),
                FlashCard(
                    question = "В чём суть MVVM?",
                    answer = "Model-View-ViewModel: View отображает состояние из ViewModel и посылает события. ViewModel обновляет Model и публикует новое состояние. Нет прямой зависимости View→Model."
                ),
                FlashCard(
                    question = "Что такое Repository паттерн?",
                    answer = "Repository — единственный источник данных для ViewModel. Скрывает детали: локальная БД, сеть или кэш. ViewModel не знает, откуда пришли данные."
                ),
            )
        ),
        FlashCardSet(
            id = "coroutines",
            title = "Coroutines",
            description = "Асинхронность в Kotlin",
            emoji = "⚡",
            cards = listOf(
                FlashCard(
                    question = "Что такое корутина?",
                    answer = "Корутина — лёгкий поток, управляемый программно. Может приостанавливаться (suspend) без блокировки системного потока и возобновляться позже."
                ),
                FlashCard(
                    question = "Что такое suspend-функция?",
                    answer = "Функция, помеченная suspend, может приостанавливать выполнение корутины. Вызывать её можно только из другой suspend-функции или из корутины."
                ),
                FlashCard(
                    question = "Чем launch отличается от async?",
                    answer = "launch запускает корутину «fire and forget», возвращает Job. async запускает корутину и возвращает Deferred<T>, результат получается через await()."
                ),
                FlashCard(
                    question = "Что такое Dispatchers?",
                    answer = "Dispatchers определяют, в каком потоке выполняется корутина: Main — главный поток, IO — пул для I/O операций, Default — для CPU-интенсивных задач."
                ),
                FlashCard(
                    question = "Что такое CoroutineScope?",
                    answer = "CoroutineScope — область жизни корутин. При отмене scope отменяются все дочерние корутины. viewModelScope привязан к жизненному циклу ViewModel."
                ),
            )
        )
    )

    fun getSetById(id: String): FlashCardSet? = cardSets.find { it.id == id }
}
