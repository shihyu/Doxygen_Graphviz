#pragma once

#include "Command.h"
#include "FileOpeUtil.h"
#include "MenuCommand.h"
#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example7
				{

					///
					/// <summary> * ��������� </summary>
					/// 
					class CommandQueue
					{
					///	
					///	 <summary> * ����ӵģ��ļ����� </summary>
					///	 
					private:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static const std::string FILE_NAME = "CmdQueue.txt";
					///	
					///	 <summary> * �����洢�������Ķ���
					///	 * ��ע�⣺����û��ʹ��java.util.Queue������Ϊ���õ�ʵ��Queue�ӿڵ�LinkedList��Ҫ���ŵĶ����ǿ�����ģ�
					///	 * ������ʹ�õ�Comparator�����Ҫ��ʾ�Ĺ���û�ж���ϵ�����������Ӹ����ԡ�
					///	 * ����һ����Ҫ�Ĺ����ǰ�����������Ⱥ�˳���źþͿ����ˣ�ֻҪ������ľͿ����ˡ�
					///	 * ���Ϊ����ʾ�ļ���ԣ���ֱ��ʹ��List�ˡ� </summary>
					///	 
						static std::vector<Command*> cmds;
//JAVA TO C++ CONVERTER TODO TASK: Static constructors are not allowed in native C++:
						static CommandQueue();
					///	
					///	 <summary> * ����Ա������һ���µĲ˵�
					///	 * ��Ҫͬ��������Ϊͬʱ���кܶ�ķ���Ա����˵�����ͬʱ���кܶ��ʦ�ڴӶ�����ȡֵ </summary>
					///	 * <param name="menu"> ����Ĳ˵� </param>
					///	 
					public:
						static void addMenu(MenuCommand *menu);
					///	
					///	 <summary> * ��ʦ��������������ȡ���������д���
					///	 * Ҳ����Ҫͬ���� </summary>
					///	 
						static Command *getOneCommand();
					};
				}
			}
		}
	}
}