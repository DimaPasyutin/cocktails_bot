package bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.TelegramFile
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.logging.LogLevel
import com.sun.jndi.toolkit.url.UrlUtil
import utils.*

class CocktailsBot {

    private var _chatId: ChatId.Id? = null
    private val chatId by lazy { requireNotNull(_chatId) }

    fun createBot(): Bot {
        return bot {
            token = API_TOKEN_BOT
            timeout = TIMEOUT_TIME
            logLevel = LogLevel.Network.Body

            dispatch {
                setUpCommands()
                setUpCallbacks()
            }
        }
    }

    private fun Dispatcher.setUpCommands() {
        command(START) {
            _chatId = ChatId.fromId(message.chat.id)

            val message = bot.sendMessage(
                chatId = chatId,
                text = "Привет! Я бот, который поможет тебе сделать коктейль! \nДля запуска бота введи команду /cocktail"
            )
            message.fold({
                bot.sendMessage(
                    chatId = chatId,
                    text = "Все чики-пукм"
                )
            },{
                bot.sendMessage(
                chatId = chatId,
                text = "Что-то пошло не по плану"
            )
            })
        }

        command(COCKTAIL) {
            _chatId = ChatId.fromId(message.chat.id)
            val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = "Для вечеринок",
                        callbackData = FOR_PARTY
                    ),
                    InlineKeyboardButton.CallbackData(
                        text = "ПП коктейли",
                        callbackData = FOR_HEALTH
                    ),
                    InlineKeyboardButton.CallbackData(
                        text = "В твоем холодосе точно будут все ингредиенты",
                        callbackData = FOR_EVERYBODY
                    )
                )
            )
            bot.sendMessage(
                chatId = chatId,
                text = "Давай определимся с форматом коктейлей",
                replyMarkup = inlineKeyboardMarkup
            )
        }
    }

    private fun Dispatcher.setUpCallbacks() {
        callbackQuery(callbackData = FOR_PARTY) {
            bot.sendMessage(chatId = chatId, text = "Держи нашу знаменитую подборку")
            bot.sendPhoto(chatId, TelegramFile.ByUrl(
                "https://avatars.mds.yandex.net/get-zen_doc/4612968/pub_60b335863204ed6102d1fcdb_60b33a35e66ec34983490f0e/scale_1200"),
                caption = "Все еще хочешь коктейль?")
        }

        callbackQuery(callbackData = FOR_HEALTH) {
            bot.sendMessage(chatId = chatId, text = "ППшник дохуя?!")
        }

        callbackQuery(callbackData = FOR_EVERYBODY) {
            bot.sendMessage(chatId = chatId, text = "Иди молока попей")
        }
    }

}