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
					/// <summary> * ��������̶��Ź��� </summary>
					/// 
					class ChopCommand : public Command
					{
					///	
					///	 <summary> * ���о������˵ĳ�ʦ�Ķ��� </summary>
					///	 
					private:
						CookApi *cookApi;
					///	
					///	 <summary> * ���þ������˵ĳ�ʦ�Ķ��� </summary>
					///	 * <param name="cookApi"> �������˵ĳ�ʦ�Ķ��� </param>
					///	 
					public:
						virtual void setCookApi(CookApi *cookApi);
					///	
					///	 <summary> * ��˵����� </summary>
					///	 
					private:
						int tableNum;
					///	
					///	 <summary> * ���췽���������˵����� </summary>
					///	 * <param name="tableNum"> ��˵����� </param>
					///	 
					public:
						ChopCommand(int tableNum);
						virtual int getTableNum();

						virtual void execute();
					};

				}
			}
		}
	}
}