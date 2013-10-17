#pragma once

#include "Command.h"
#include "MainBoardApi.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example3
				{
					///
					/// <summary> * �������������ʵ�֣�ʵ��Command�ӿڣ�
					/// * ���������������������ʵ�֣�ͨ�����ý����ߵķ�����ʵ������ </summary>
					/// 
					class ResetCommand : public Command
					{
					///	
					///	 <summary> * ��������ʵ������Ľ����ߡ���������� </summary>
					///	 
					private:
						MainBoardApi *mainBoard;
					///	
					///	 <summary> * ���췽��������������� </summary>
					///	 * <param name="mainBoard"> ������� </param>
					///	 
					public:
						ResetCommand(MainBoardApi *mainBoard);

						virtual void execute();
					};

				}
			}
		}
	}
}