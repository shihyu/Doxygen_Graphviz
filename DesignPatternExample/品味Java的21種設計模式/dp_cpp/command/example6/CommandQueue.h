#pragma once

#include "Command.h"
#include "MenuCommand.h"
#include <vector>

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
					/// <summary> * ��������� </summary>
					/// 
					class CommandQueue
					{
					///	
					///	 <summary> * �����洢�������Ķ���
					///	 * ��ע�⣺����û��ʹ��java.util.Queue������Ϊ���õ�ʵ��Queue�ӿڵ�LinkedList��Ҫ���ŵĶ����ǿ�����ģ�
					///	 * ������ʹ�õ�Comparator�����Ҫ��ʾ�Ĺ���û�ж���ϵ�����������Ӹ����ԡ�
					///	 * ����һ����Ҫ�Ĺ����ǰ�����������Ⱥ�˳���źþͿ����ˣ�ֻҪ������ľͿ����ˡ�
					///	 * ���Ϊ����ʾ�ļ���ԣ���ֱ��ʹ��List�ˡ� </summary>
					///	 
					private:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static std::vector<Command*> cmds = std::vector<Command*>();
					///	
					///	 <summary> * ����Ա������һ���µĲ˵�
					///	 * ��Ҫͬ��������Ϊͬʱ���кܶ�ķ���Ա����˵�����ͬʱ���кܶ��ʦ�ڴӶ�����ȡֵ </summary>
					///	 * <param name="menu"> ����Ĳ˵� </param>
					///	 
					public:
						static void addMenu(MenuCommand *menu);
					///	
					///	 <summary> * ��ʦ��������������ȡ���������д���Ҳ����Ҫͬ���� </summary>
					///	 
						static Command *getOneCommand();
					};

				}
			}
		}
	}
}