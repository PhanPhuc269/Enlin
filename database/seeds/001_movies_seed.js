/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.seed = async function(knex) {
  // Deletes ALL existing entries
  await knex('movie').del();

  // Insert seed entries
  await knex('movie').insert([
    {
      title: 'Godzilla x Kong: Đế chế mới',
      description: 'Hai quái vật huyền thoại hợp lực chống lại mối đe dọa từ thế giới ngầm.',
      genre: 'Hành động, Khoa học viễn tưởng',
      duration: 115,
      director: 'Adam Wingard',
      actors: 'Rebecca Hall, Dan Stevens, Brian Tyree Henry',
      language: 'Tiếng Anh',
      trailer_url: 'https://www.youtube.com/watch?v=GodzillaKongTrailer',
      poster_url: 'https://cinestar.com.vn/pictures/Godzilla-x-Kong.jpg',
      release_date: '2024-04-26'
    },
    {
      title: 'Mai',
      description: 'Câu chuyện cảm động về cuộc đời một người phụ nữ tên Mai.',
      genre: 'Tâm lý, Tình cảm',
      duration: 130,
      director: 'Trấn Thành',
      actors: 'Phương Anh Đào, Tuấn Trần',
      language: 'Tiếng Việt',
      trailer_url: 'https://www.youtube.com/watch?v=MaiMovieTrailer',
      poster_url: 'https://cinestar.com.vn/pictures/Mai.jpg',
      release_date: '2024-02-10'
    },
    {
      title: 'Kungfu Panda 4',
      description: 'Po phải huấn luyện một chiến binh rồng mới trong khi đối mặt với kẻ phản diện đầy mưu mô.',
      genre: 'Hoạt hình, Hài hước, Phiêu lưu',
      duration: 94,
      director: 'Mike Mitchell',
      actors: 'Jack Black, Awkwafina, Viola Davis',
      language: 'Tiếng Anh',
      trailer_url: 'https://www.youtube.com/watch?v=KungfuPanda4Trailer',
      poster_url: 'https://cinestar.com.vn/pictures/Kungfu-Panda-4.jpg',
      release_date: '2024-03-22'
    }
  ]);
};
