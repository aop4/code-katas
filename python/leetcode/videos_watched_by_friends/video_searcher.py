from typing import List, Set
from collections import Counter

class VideoSearcher:
    """
    Solution for a graph-searching problem involving a search for nth-degree relationships.
    """

    def videos_watched_by_friends(self, watched_videos: List[List[str]], friend_graph: List[List[int]], user_id: int,
                                  degree: int) -> List[str]:
        """Entrypoint for the algorithm. Retrieves a list of videos watched by users who are separated from the
        specified user by the specified number of degrees.

        Args:
            watched_videos: Contains the videos watched by each user. The ith item of the list contains all videos
            watched by the ith user.
            friend_graph: Contains each user's friend list. The ith item contains all the users who are friends
              with user i. All friendships are bidirectional: if friend_graph[i] contains k, then friend_graph[k]
              contains i.
            user_id: The user id of the user who is the origin of the search.
            degree: The number of connections to traverse during the search. A value of 1 will retrieve direct friends'
              videos, a value of 2 will retrieve friends of friends' videos, and so on.
        Returns:
            A list of videos watched by the user's nth-degree connections. Videos watched by closer connections are
            excluded. Videos are sorted primarily by frequency of occurrence (descending), then by title (ascending).
        """
        # Catch an absurdly high degree that would unnecessarily waste processing power
        if degree >= len(friend_graph):
            return []
        if degree < 0:
            raise ValueError('degree cannot be negative')
        friends_n_degrees_away = self._get_friends_n_degrees_away(friend_graph, user_id, degree)
        return self._get_videos_watched_by(friends_n_degrees_away, watched_videos)

    def _get_friends_n_degrees_away(self, friend_graph: List[List[int]], user_id: int, degree: int):
        """Visible for testing.
        Retrieves a list of users who are separated from the user with id user_id by exactly degree connections.
        For example, if degree is 2, retrieves all friends of friends, excluding the original user and
        their first-degree friends."""
        visited = set([user_id])
        friends_at_degree = [user_id]
        for i in range(degree):
            friends_at_degree = self._collect_unvisited_friends(friends_at_degree, friend_graph, visited)
            # if no connections at the nth degree, there's no point in checking the n+1th
            if not friends_at_degree:
                break
        return friends_at_degree

    def _collect_unvisited_friends(self, users: List[int], friend_graph: List[List[int]], visited: Set[int]):
        """Retrieves all the friends of the users list, who have not been visited thus far."""
        unvisited_friends = []
        for user in users:
            for friend in friend_graph[user]:
                if friend not in visited:
                    unvisited_friends.append(friend)
                    visited.add(friend)
        return unvisited_friends

    def _get_videos_watched_by(self, users: List[int], watched_videos: List[List[str]]):
        """Retrieves all videos watched by the users list, sorted by frequency (descending) and title (ascending)."""
        videos = Counter()
        for user in users:
            for video in watched_videos[user]:
                videos[video] += 1
        # sort by title
        output = sorted(videos.keys())
        # sort by frequency. Supposed to be an ascending sort, but I chose descending for this implementation.
        output.sort(key=videos.get, reverse=True)
        return output
