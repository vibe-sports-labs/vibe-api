package com.vibe.infrastructure.security

import com.google.firebase.auth.FirebaseAuth
import com.vibe.application.service.UserService
import com.vibe.domain.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.env.Environment

class FirebaseFilter(private val userService: UserService, private val environment: Environment) :
    OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val impersonateUser = request.getHeader("X-Impersonate-User")
        if (environment.activeProfiles.contains("dev") && impersonateUser != null) {
            val userRecord = FirebaseAuth.getInstance().getUser(impersonateUser)
            SecurityContextHolder.getContext().authentication =
                authenticateUser(userRecord.uid, userRecord.email, userRecord.displayName, userRecord.photoUrl)
        } else {
            request.getHeader("Authorization")?.takeIf { it.startsWith("Bearer ") }?.let { header ->
                val token = header.substring(7)
                try {
                    val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
                    SecurityContextHolder.getContext().authentication =
                        authenticateUser(decodedToken.uid, decodedToken.email, decodedToken.name, decodedToken.picture)
                } catch (_: Exception) {
                    SecurityContextHolder.clearContext()
                }
            }
        }

        chain.doFilter(request, response)
    }

    private fun authenticateUser(
        uid: String,
        email: String,
        name: String?,
        photoUrl: String?
    ): UsernamePasswordAuthenticationToken {
        val user = userService.syncUser(uid, email, name, photoUrl)
        val authentication = UsernamePasswordAuthenticationToken(
            uid,
            null,
            user.roles.map { SimpleGrantedAuthority(it.name) }
        )

        return authentication
    }
}