#pragma once

#include "Command.h"
#include "CookApi.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example6
				{
					///
					/// <summary> * ������󣬱�����Ѽ </summary>
					/// 
					class DuckCommand : public Command
					{
					private:
						CookApi *cookApi;
					public:
						virtual void setCookApi(CookApi *cookApi);
					private:
						int tableNum;
					public:
						DuckCommand(int tableNum);
						virtual int getTableNum();

						virtual void execute();
					};

				}
			}
		}
	}
}