#pragma once

#include "Command.h"
#include "OperationApi.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example4
				{
					///
					/// <summary> * ����ļ�������ʵ�ֶ��� </summary>
					/// 
					class SubstractCommand : public Command
					{
					///	
					///	 <summary> * ���о���ִ�м���Ķ��� </summary>
					///	 
					private:
						OperationApi *operation;
					///	
					///	 <summary> * ���������ݣ�Ҳ����Ҫ��ȥ������ </summary>
					///	 
						int opeNum;
					///	
					///	 <summary> * ���췽�����������ִ�м���Ķ��� </summary>
					///	 * <param name="operation"> ����ִ�м���Ķ��� </param>
					///	 * <param name="opeNum"> Ҫ��ȥ������ </param>
					///	 
					public:
						SubstractCommand(OperationApi *operation, int opeNum);

						virtual void execute();

						virtual void undo();
					};

				}
			}
		}
	}
}