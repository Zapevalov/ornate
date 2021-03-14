package site

import org.ornate.WebSite
import org.ornate.annotation.Page
import site.pages.MainPage

interface OppabetSite : WebSite {
    @Page
    fun onMainPage(): MainPage
}