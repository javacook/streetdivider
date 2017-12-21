package de.kotlincook.textmining.streetdivider

import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.scene.text.FontWeight
import tornadofx.*

// A GUI to test the StreetDivider quickly

class Styles : Stylesheet() {
    init {
        label {
            fontSize = 14.px
            fontWeight = FontWeight.NORMAL
        }
    }
}

class MyApp: App(MyView::class, Styles::class)

class MyView: View() {

    val streetDivider = StreetDivider()
    val streetProp = SimpleStringProperty()
    val houseNumberProp = SimpleStringProperty()
    val houseNoAffixProp = SimpleStringProperty()

    override val root = form {
        fieldset("Geben Sie eine Straße mit Hausnummer und Zusatz ein:") {
            field("Eingabe") {
                textfield() {
                    textProperty().addListener { _, _, new ->
                        with(streetDivider.parse(new)) {
                            streetProp.set(street)
                            houseNumberProp.set(houseNumber?.toString())
                            houseNoAffixProp.set(houseNoAffix)
                        }
                    }
                }
            }
            field("Straße") {
                label().bind(streetProp)
            }
            field("Hausnummer") {
                label().bind(houseNumberProp)
            }
            field("Zusatz") {
                label().bind(houseNoAffixProp)
            }
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(MyApp::class.java, *args)
}