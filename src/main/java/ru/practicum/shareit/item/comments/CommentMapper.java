package ru.practicum.shareit.item.comments;

import ru.practicum.shareit.user.model.User;

public class CommentMapper {
    public static Comment toComment(CommentDto commentDto, User author) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getItem(),
                author,
                commentDto.getCreated()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}
