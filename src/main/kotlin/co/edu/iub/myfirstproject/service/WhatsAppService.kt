package co.edu.iub.myfirstproject.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.support.BasicAuthenticationInterceptor
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class WhatsAppService(
    @param:Value("\${app.twilio.account-sid}") private val accountSid: String,
    @param:Value("\${app.twilio.auth-token}") private val authToken: String,
    @param:Value("\${app.twilio.whatsapp-from}") private val whatsappFrom: String
) {

    private val logger = LoggerFactory.getLogger(WhatsAppService::class.java)

    private val restTemplate = RestTemplate().apply {
        interceptors.add(BasicAuthenticationInterceptor(accountSid, authToken))
    }

    fun sendReminder(toNumber: String, mensaje: String): Boolean {
        return try {
            val url = "https://api.twilio.com/2010-04-01/Accounts/$accountSid/Messages.json"

            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

            val body: MultiValueMap<String, String> = LinkedMultiValueMap()
            body.add("From", "whatsapp:$whatsappFrom")
            body.add("To", "whatsapp:$toNumber")
            body.add("Body", mensaje)

            restTemplate.postForEntity(url, HttpEntity(body, headers), String::class.java)
            true
        } catch (ex: Exception) {
            logger.error("Error enviando WhatsApp a $toNumber", ex)
            false
        }
    }
}

