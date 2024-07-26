package com.songify;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
public class SongsController {

    Map<Integer, String> database = new HashMap<>(Map.of(
            1, "shawnmendes song 1",
            2, "ariana grande song 2",
            3, "the weekend song 3",
            4, "imagine dragons song 4"
    ));

    @GetMapping("/songs")
    public ResponseEntity<SongResponseDto> getAllSongs() {

        SongResponseDto response = new SongResponseDto(database);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<SingleSongResponseDto> getSongById(
            @PathVariable Integer id,
            @RequestHeader(required = false) String requestId) {

        log.info(requestId);
        String song = database.get(id);

        if (song == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        SingleSongResponseDto response = new SingleSongResponseDto(song);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/songs")
    public ResponseEntity<SingleSongResponseDto> postSong(@RequestBody SongRequestDto request) {
        String songName = request.songName();
        log.info("adding new song {}", songName);
        database.put(database.size() + 1, songName);

        return ResponseEntity.ok(new SingleSongResponseDto(songName));
    }
}
