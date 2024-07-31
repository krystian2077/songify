package com.songify.song.controller;

import com.songify.song.dto.request.CreateSongRequestDto;
import com.songify.song.dto.request.PartiallyUpdateSongRequestDto;
import com.songify.song.dto.request.UpdateSongRequestDto;
import com.songify.song.dto.response.*;
import com.songify.song.error.SongNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/songs")
public class SongRestController {

    Map<Integer, Song> database = new HashMap<>(Map.of(
            1, new Song("Treat You Better", "Shawn Mendes"),
            2, new Song("Impossible", "James Arthur"),
            3, new Song("Greatest", "Sia"),
            4, new Song("Demons", "Imagine Dragons")

    ));

    @GetMapping()
    public ResponseEntity<GetAllSongsResponseDto> getAllSongs() {

        GetAllSongsResponseDto response = new GetAllSongsResponseDto(database);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSongResponseDto> getSongById(
            @PathVariable Integer id,
            @RequestHeader(required = false) String requestId) {

        log.info(requestId);

        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }

        Song song = database.get(id);
        GetSongResponseDto response = new GetSongResponseDto(song);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<CreateSongResponseDto> postSong(@RequestBody @Valid CreateSongRequestDto request) {
        Song song = new Song(request.songName(), request.artist());
        log.info("adding new song {}", song);
        database.put(database.size() + 1, song);

        return ResponseEntity.ok(new CreateSongResponseDto(song));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongByIdUsingPathVariable(@PathVariable Integer id) {
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }

        database.remove(id);
        return ResponseEntity.ok(new DeleteSongResponseDto("You deleted song with id " + id, HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateSongResponseDto> update(@PathVariable Integer id,
                                                        @RequestBody @Valid UpdateSongRequestDto request) {

        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }

        String newSongName = request.songName();
        String newArtist = request.artist();
        Song newSong = new Song(newSongName, newArtist);
        Song oldSong = database.put(id, newSong);
        log.info("Updating song with id: {} with songName: {} to newSongName: {} oldArtist: {} to newArtis:t {}",
                id, oldSong.name(), newSong.name(), oldSong.artist(), newSong.artist());


        return ResponseEntity.ok(new UpdateSongResponseDto(newSong.name(), newSong.artist()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateSongResponseDto>
    partiallyUpdateSong(@PathVariable Integer id, @RequestBody PartiallyUpdateSongRequestDto request) {

        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        Song songFromDatabase = database.get(id);
        Song.SongBuilder builder = Song.builder();
        if (request.songName() != null) {
            builder.name(request.songName());
            log.info("partially updated song name");
        } else {
            builder.name(songFromDatabase.name());
        }
        if (request.artist() != null) {
            builder.artist(request.artist());
            log.info("partially updated artist");
        } else {
            builder.artist(songFromDatabase.artist());
        }
        Song updatedSong = builder.build();
        database.put(id, updatedSong);

        return ResponseEntity.ok(new PartiallyUpdateSongResponseDto(updatedSong));

    }
}
