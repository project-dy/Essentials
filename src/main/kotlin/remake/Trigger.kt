package remake

import com.neovisionaries.i18n.CountryCode
import essentials.internal.Tool
import mindustry.gen.Playerc
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.UnknownHostException
import java.util.*

object Trigger {
    fun load(){

    }

    fun loadPlayer(player: Playerc, data: DB.PlayerData){
        player.name(data.name)
        data.lastdate = System.currentTimeMillis()
        data.joincount++
    }

    fun createPlayer(player: Playerc, password: String?){
        val data = DB.PlayerData

        val ip = player.ip()
        val isLocal = try {
            val address = InetAddress.getByName(ip)
            if(address.isAnyLocalAddress || address.isLoopbackAddress) {
                true
            } else {
                NetworkInterface.getByInetAddress(address) != null
            }
        } catch(e: SocketException) {
            false
        } catch(e: UnknownHostException) {
            false
        }

        data.countryCode = if (isLocal) {
            Locale.getDefault().isO3Country
        } else {
            val res = Tool.ipre.IPQuery(ip)
            val code = CountryCode.getByCode(res.countryShort)
            if(code == null) Locale.getDefault().isO3Country else code.toLocale().isO3Country
        }

        data.name = player.name()
        data.uuid = player.uuid()
        data.joinDate = System.currentTimeMillis()
        data.id = player.name()
        data.pw = password ?: player.name()

        DB.createData(data)
    }
}