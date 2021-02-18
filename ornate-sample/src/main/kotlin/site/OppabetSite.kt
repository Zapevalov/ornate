package site

import org.ornate.WebSite
import org.ornate.extension.Page
import site.pages.MainPage

interface OppabetSite : WebSite {
    @Page
    fun onMainPage(): MainPage
}