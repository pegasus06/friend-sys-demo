package com.ryan.userCenter.service;

import com.ryan.userCenter.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryan.userCenter.domain.User;
import com.ryan.userCenter.domain.dto.TeamQuery;
import com.ryan.userCenter.domain.request.TeamJoinRequest;
import com.ryan.userCenter.domain.request.TeamQuitRequest;
import com.ryan.userCenter.domain.request.TeamUpdateRequest;
import com.ryan.userCenter.domain.vo.TeamUserVO;

import java.util.List;

/**
* @author RZ
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2025-09-26 17:00:22
*/
public interface TeamService extends IService<Team> {
    long addTeam(Team team, User loginUser);
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);
    /**
     * 更新队伍
     *
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     *
     * @param teamJoinRequest
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     *
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);


    /**
     * 删除（解散）队伍
     *
     * @param id
     * @param loginUser
     * @return
     */
    boolean deleteTeam(long id, User loginUser);
}
