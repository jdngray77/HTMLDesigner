package com.jdngray77.shamelessclonecraft

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder

class Cube(model: Model) : ModelInstance(model){
    companion object {
        val builder = ModelBuilder()

        val Cubes : ArrayList<Model> = arrayListOf()

        val m = Material(TextureAttribute.createDiffuse(Texture("badlogic.jpg")))

        val attr = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()

        @JvmStatic
        fun create() : Cube =
            builder.createBox(5f, 5f, 5f, m, attr).let {
                Cubes.add(it)
                Cube(it)
            }
        }
}