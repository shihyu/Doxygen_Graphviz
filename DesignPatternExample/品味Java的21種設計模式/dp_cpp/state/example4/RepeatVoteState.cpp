#include "RepeatVoteState.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace state
			{
				namespace example4
				{

					void RepeatVoteState::vote(std::string user, std::string voteItem, VoteManager *voteManager)
					{
						//重复投票
						//暂时不做处理
						puts("请不要重复投票");

						//重复投票完成，维护下一个状态，重复投票到5次，就算恶意投票了
						//注意这里是判断大于等于4，因为这里设置的是下一个状态
						//下一个操作次数就是5了，就应该算是恶意投票了
						if(voteManager->getMapVoteCount()->get(user) >= 4)
						{
							voteManager->getMapState()->put(user, new SpiteVoteState());
						}
					}
				}
			}
		}
	}
}
