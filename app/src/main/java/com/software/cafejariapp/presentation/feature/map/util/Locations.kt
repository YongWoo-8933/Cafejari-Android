package com.software.cafejariapp.presentation.feature.map.util

import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition


data class Locations(
    val name: String,
    val cameraPosition: CameraPosition
) {

    companion object {
        val koreaLatLngBounds = LatLngBounds(
            LatLng(32.724435, 125.469936),
            LatLng(38.447639, 131.995789),
        )
        val sinchon = Locations(
            name = "신촌역", cameraPosition = CameraPosition(
                LatLng(37.55649747287372, 126.93710302643744), Zoom.MEDIUM
            )
        )
        private val hongik = Locations(
            name = "홍대입구역", cameraPosition = CameraPosition(
                LatLng(37.557176, 126.924175), Zoom.MEDIUM
            )
        )
        private val ewha = Locations(
            name = "이대역", cameraPosition = CameraPosition(
                LatLng(37.557407, 126.945836), Zoom.MEDIUM
            )
        )
        private val hyehwa = Locations(
            name = "혜화(대학로)역", cameraPosition = CameraPosition(
                LatLng(37.582351, 127.001308), Zoom.MEDIUM
            )
        )
        private val konkuk = Locations(
            name = "건대입구역", cameraPosition = CameraPosition(
                LatLng(37.540778, 127.071034), Zoom.MEDIUM
            )
        )
        private val wangsimni = Locations(
            name = "왕십리역", cameraPosition = CameraPosition(
                LatLng(37.561233, 127.039957), Zoom.MEDIUM
            )
        )
        private val anam = Locations(
            name = "안암(고려대)역", cameraPosition = CameraPosition(
                LatLng(37.586277, 127.028590), Zoom.MEDIUM
            )
        )
        private val heukseok = Locations(
            name = "흑석역", cameraPosition = CameraPosition(
                LatLng(37.508567, 126.963309), Zoom.MEDIUM
            )
        )
        private val seoulUniv = Locations(
            name = "서울대입구역", cameraPosition = CameraPosition(
                LatLng(37.481423, 126.952701), Zoom.MEDIUM
            )
        )
        private val sillim = Locations(
            name = "신림역", cameraPosition = CameraPosition(
                LatLng(37.484744, 126.929578), Zoom.MEDIUM
            )
        )
        private val foreignUniv = Locations(
            name = "외대앞역", cameraPosition = CameraPosition(
                LatLng(37.595448, 127.059551), Zoom.MEDIUM
            )
        )
        private val hoegi = Locations(
            name = "회기역", cameraPosition = CameraPosition(
                LatLng(37.589647, 127.057347), Zoom.MEDIUM
            )
        )
        private val kwangWoon = Locations(
            name = "광운대역", cameraPosition = CameraPosition(
                LatLng(37.620769, 127.058921), Zoom.MEDIUM
            )
        )
        private val noryangjin = Locations(
            name = "노량진역", cameraPosition = CameraPosition(
                LatLng(37.513058, 126.942192), Zoom.MEDIUM
            )
        )
        private val yeongjong = Locations(
            name = "영종도(인천)", cameraPosition = CameraPosition(
                LatLng(37.494459, 126.552045), Zoom.MEDIUM
            )
        )
        val locationList = listOf(
            kwangWoon,
            konkuk,
            noryangjin,
            seoulUniv,
            sillim,
            sinchon,
            anam,
            wangsimni,
            foreignUniv,
            ewha,
            hyehwa,
            hongik,
            hoegi,
            heukseok,
        )
    }
}