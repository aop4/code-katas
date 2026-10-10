import unittest
from unittest.mock import patch
from leetcode.videos_watched_by_friends.video_searcher import VideoSearcher

class TestVideoSearcher(unittest.TestCase):
    def setUp(self) -> None:
        self.video_searcher = VideoSearcher()

    def test_videos_watched_by_friends_negative_degree(self):
        with self.assertRaises(ValueError):
            self.video_searcher.videos_watched_by_friends([['I, Robot'], ['Zoolander']],
                                                          [[1], [0]], 0, -1)
    
    def test_videos_watched_by_friends_zero_degree(self):
        videos = self.video_searcher.videos_watched_by_friends([['I, Robot'], ['Zoolander']],
                                                               [[1], [0]], 0, 0)
        self.assertEqual(['I, Robot'], videos)

    @patch.object(VideoSearcher, '_get_friends_n_degrees_away')
    def test_videos_watched_by_friends_degree_too_large_for_data(self, mock_method):
        videos = self.video_searcher.videos_watched_by_friends([['I, Robot'], ['Zoolander']],
                                                               [[1], [0]], 0, 2147483647)
        self.assertEqual([], videos)
        self.assertFalse(mock_method.called)

    def test_videos_watched_by_friends_user_has_no_friends(self):
        videos = self.video_searcher.videos_watched_by_friends([['I, Robot'], ['Zoolander', 'The Matrix'], ['X-Men', 'The Matrix']],
                                                               [[], [2], [1]], 0, 1)
        self.assertEqual([], videos)

    def test_videos_watched_by_friends_connections_have_no_videos(self):
        videos = self.video_searcher.videos_watched_by_friends([['I, Robot'], [], []],
                                                               [[1,2], [0], [0]], 0, 1)
        self.assertEqual([], videos)

    def test_videos_watched_by_friends_first_degree(self):
        videos = self.video_searcher.videos_watched_by_friends([['I, Robot'], ['Zoolander', 'The Matrix'], ['X-Men', 'The Matrix']],
                                                               [[1,2], [0], [0]], 0, 1)
        self.assertEqual(['The Matrix', 'X-Men', 'Zoolander'], videos)
    
    def test_videos_watched_by_friends_second_degree(self):
        videos = self.video_searcher.videos_watched_by_friends([['I, Robot'], ['Zoolander', 'The Matrix'], ['X-Men', 'The Matrix'], ['Madea']],
                                                               [[1,2], [0,2], [0,1,3], [2]], 0, 2)
        self.assertEqual(['Madea'], videos)

    def test_videos_watched_by_friends_third_degree(self):
        videos = self.video_searcher.videos_watched_by_friends([['I, Robot'], ['Zoolander'], ['X-Men'], ['Madea']],
                                                               [[1], [2,0], [1,3], [2]], 3, 3)
        self.assertEqual(['I, Robot'], videos)
    
    def test_videos_watched_by_friends_sorting(self):
        videos = self.video_searcher.videos_watched_by_friends([['I, Robot'], ['Avatar', 'The Matrix'], ['X-Men', 'The Matrix'],
                                                                ["Pan's Labyrinth", 'The Matrix', 'Avatar'], ['The Matrix']],
                                                               [[1,2,3,4], [0], [0], [0], [0]], 0, 1)
        self.assertEqual(['The Matrix', 'Avatar', "Pan's Labyrinth", 'X-Men'], videos)
