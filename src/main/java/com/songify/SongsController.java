package com.songify;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
public class SongsController {

    Map<Integer, String> database = new HashMap<>();

    @GetMapping("/songs")
    public ResponseEntity<SongResponseDto> getAllSongs() {
        database.put(1, "shawnmendes song1");
        database.put(2, "ariana grande song2");
        SongResponseDto response = new SongResponseDto(database);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<SingleSongResponseDto> getSongById(@PathVariable Integer id, @RequestHeader(required = false) String requestId) {
        log.info(requestId);
        String song = database.get(id);

        if (song == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }

        SingleSongResponseDto response = new SingleSongResponseDto(song);
        return ResponseEntity.ok(response);
    }
}
