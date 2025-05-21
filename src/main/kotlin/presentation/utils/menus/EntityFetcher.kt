package org.example.presentation.utils.menus

import org.example.presentation.utils.io.UiDisplayer
import java.util.*

class EntityFetcher {
    suspend fun <T> fetchEntities(
        ui: UiDisplayer,
        fetchUseCase: suspend () -> List<T>,
        entityName: String
    ): List<T> {
        ui.displayMessage("🔹 Fetching all $entityName...")
        return fetchUseCase()
    }

    suspend fun <T> fetchEntitiesForContext(
        ui: UiDisplayer,
        fetchUseCase: suspend (UUID) -> List<T>,
        contextId: UUID,
        entityName: String,
        contextName: String
    ): List<T> {
        ui.displayMessage("🔹 Fetching $entityName for $contextName...")
        return fetchUseCase(contextId)
    }
}