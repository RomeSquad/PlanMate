package org.example.logic.repository

import org.example.logic.entity.Project

interface ChangeHistoryRepository {
        fun getHistoryByProjectId(projectId: Int): Result<Project>
}