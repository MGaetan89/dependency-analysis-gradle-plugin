// Copyright (c) 2024. Tony Robalik.
// SPDX-License-Identifier: Apache-2.0
package com.autonomousapps.visitor

import com.autonomousapps.internal.graph.supers.SuperClassGraphBuilder
import com.autonomousapps.internal.graph.supers.SuperNode
import com.autonomousapps.internal.unsafeLazy
import com.autonomousapps.model.DuplicateClass
import com.autonomousapps.model.declaration.internal.Declaration
import com.autonomousapps.model.internal.Dependency
import com.autonomousapps.model.internal.DependencyGraphView
import com.autonomousapps.model.internal.ProjectVariant
import com.google.common.graph.Graph

internal class GraphViewReader(
  private val project: ProjectVariant,
  private val dependencies: Set<Dependency>,
  private val graph: DependencyGraphView,
  private val declarations: Set<Declaration>,
  private val duplicateClasses: Set<DuplicateClass>,
) {

  fun accept(visitor: GraphViewVisitor) {
    val context = DefaultContext(project, dependencies, graph, declarations, duplicateClasses)
    dependencies.forEach { dependency ->
      visitor.visit(dependency, context)
    }
  }
}

internal class DefaultContext(
  override val project: ProjectVariant,
  override val dependencies: Set<Dependency>,
  override val graph: DependencyGraphView,
  override val declarations: Set<Declaration>,
  override val duplicateClasses: Set<DuplicateClass>,
) : GraphViewVisitor.Context {

  override val superGraph: Graph<SuperNode> by unsafeLazy {
    SuperClassGraphBuilder.of(dependencies)
  }
}
