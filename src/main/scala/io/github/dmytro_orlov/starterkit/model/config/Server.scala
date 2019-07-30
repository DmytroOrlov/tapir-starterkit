package io.github.dmytro_orlov.starterkit.model.config

import java.net.InetAddress

import eu.timepit.refined.types.net.UserPortNumber

final case class Server(host: InetAddress, port: UserPortNumber)
